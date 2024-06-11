package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.SendChatMessageDto;
import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.PrivateChatMessage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.repository.PrivateChatMessageRepository;
import expresstalk.dev.backend.repository.PrivateChatRepository;
import expresstalk.dev.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
public class PrivateChatService {
    private final PrivateChatMessageRepository privateChatMessageRepository;
    private final PrivateChatRepository privateChatRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public PrivateChatService(PrivateChatMessageRepository privateChatMessageRepository, PrivateChatRepository chatRoomRepository, UserRepository userRepository, UserService userService) {
        this.privateChatMessageRepository = privateChatMessageRepository;
        this.privateChatRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    private PrivateChat getChat(User member1, User member2) {
        PrivateChat chat = privateChatRepository.findPrivateChatBy(member1, member2);

        if(chat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat with " + member1.getLogin() + " and " + member2.getLogin() + " doesn't exist.");
        }

        return chat;
    }
    public PrivateChat getChat(UUID chatId) {
        PrivateChat chat = privateChatRepository.findById(chatId).orElse(null);

        if(chat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat with id: " + chatId + " doesn't exist.");
        }

        return chat;
    }

    public PrivateChat createPrivateChat(UUID member1Id, UUID member2Id) {
        if(member1Id.equals(member2Id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not create private chat with 1 person");
        }

        User user1 = userService.findById(member1Id);
        User user2 = userService.findById(member2Id);

        try {
            getChat(user1, user2); // throws exception if chat between two users wasn't found
        } catch (Exception ex) {
            PrivateChat chat = new PrivateChat();

            chat.getMembers().add(user1);
            chat.getMembers().add(user2);
            user1.getPrivateChats().add(chat);
            user2.getPrivateChats().add(chat);

            privateChatRepository.save(chat);
            userRepository.save(user1);
            userRepository.save(user2);

            return chat;
        }

        throw new ResponseStatusException(HttpStatus.CONFLICT, "Private chat with provided two members had already been created");

    }

    public PrivateChatMessage saveMessage(SendChatMessageDto sendPrivateChatMessageDto, User sender, User receiver) {
        PrivateChat privateChat = getChat(UUID.fromString(sendPrivateChatMessageDto.chatId()));

        PrivateChatMessage privateChatMessage = new PrivateChatMessage(
                sender,
                receiver,
                sendPrivateChatMessageDto.content(),
                new Date(Long.parseLong(sendPrivateChatMessageDto.createdAt()))
        );

        privateChat.getMessages().add(privateChatMessage);
        privateChatMessage.setPrivateChat(privateChat);
        privateChatRepository.save(privateChat);
        privateChatMessageRepository.save(privateChatMessage);

        return privateChatMessage;
    }

    private boolean isUserExistsInChat(User user, PrivateChat privateChat) {
        for (User member : privateChat.getMembers()) {
            if(member.getId().equals(user.getId())) {
                return true;
            }
        }

        return false;
    }

    public boolean isUserExistsInChat(UUID userId, UUID chatId) {
        PrivateChat privateChat = getChat(chatId);
        User user = userService.findById(userId);

        return isUserExistsInChat(user, privateChat);
    }

    public boolean isUserExistsInChat(User user, UUID chatId) {
        PrivateChat privateChat = getChat(chatId);

        return isUserExistsInChat(user, privateChat);
    }

    public void ensureUserPermissionToSendMessageInChat(User user, UUID chatId) {
        if(!isUserExistsInChat(user, chatId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User with id " + user.getId() + " can't send messages to other people's chat");
        }
    }

    public User getSecondUserOfChat(UUID firstUserId, PrivateChat privateChat) throws Exception {
        if(!isUserExistsInChat(firstUserId, privateChat.getId())) {
            throw new Exception("Searching of the second user of chat was failed because the first user doesn't exist in chat");
        }

        User secondUser = new User();

        for (User user : privateChat.getMembers()) {
            if(!user.getId().equals(firstUserId)) secondUser = user;
        }

        return secondUser;
    }

    public User getSecondUserOfChat(UUID firstUserId, UUID privateChatId) throws Exception {
        PrivateChat privateChat = getChat(privateChatId);

        if(!isUserExistsInChat(firstUserId, privateChat.getId())) {
            throw new Exception("Searching of the second user of chat was failed because the first user doesn't exist in chat");
        }

        User secondUser = new User();

        for (User user : privateChat.getMembers()) {
            if(!user.getId().equals(firstUserId)) secondUser = user;
        }

        return secondUser;
    }
}
