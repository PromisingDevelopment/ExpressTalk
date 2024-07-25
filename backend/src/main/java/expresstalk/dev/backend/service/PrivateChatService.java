package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.SendChatMessageDto;
import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.PrivateChatAccount;
import expresstalk.dev.backend.entity.PrivateMessage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.repository.PrivateChatAccountRepository;
import expresstalk.dev.backend.repository.PrivateChatRepository;
import expresstalk.dev.backend.repository.PrivateMessageRepository;
import expresstalk.dev.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrivateChatService {
    private final AccountService accountService;
    private final PrivateMessageRepository privateMessageRepository;
    private final PrivateChatAccountRepository privateChatAccountRepository;
    private final PrivateChatRepository privateChatRepository;
    private final UserRepository userRepository;

    private PrivateChat getChat(User member1, User member2) {
        PrivateChat chat = privateChatRepository.findPrivateChatBetween(member1, member2);

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

    public PrivateChat createPrivateChat(User user1, User user2) {
        if(user1.getId().equals(user2.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not create private chat with 1 person");
        }

        try {
            getChat(user1, user2); // throws exception if chat between two users wasn't found
        } catch (Exception ex) {
            PrivateChat chat = new PrivateChat();
            PrivateChatAccount account1 = new PrivateChatAccount();
            PrivateChatAccount account2 = new PrivateChatAccount();

            account1.setUser(user1);
            account1.setPrivateChat(chat);
            account2.setUser(user2);
            account2.setPrivateChat(chat);
            chat.getMembers().add(account1);
            chat.getMembers().add(account2);
            user1.getPrivateChatAccounts().add(account1);
            user2.getPrivateChatAccounts().add(account2);

            privateChatRepository.save(chat);
            privateChatAccountRepository.saveAll(Arrays.asList(account1, account2));
            userRepository.saveAll(Arrays.asList(user1, user2));

            return chat;
        }

        throw new ResponseStatusException(HttpStatus.CONFLICT, "Private chat with provided two members had already been created");

    }

    public PrivateMessage saveMessage(SendChatMessageDto sendPrivateChatMessageDto, User sender, User receiver) {
        PrivateChat privateChat = getChat(UUID.fromString(sendPrivateChatMessageDto.chatId()));
        PrivateChatAccount senderAccount = accountService.getPrivateChatAccount(sender, privateChat);
        PrivateChatAccount receiverAccount = accountService.getPrivateChatAccount(receiver, privateChat);
        PrivateMessage privateMessage = new PrivateMessage(
                senderAccount,
                receiverAccount,
                privateChat,
                sendPrivateChatMessageDto.content(),
                new Date(Long.parseLong(sendPrivateChatMessageDto.createdAt()))
        );

        senderAccount.getSentMessages().add(privateMessage);
        receiverAccount.getReceivedMessages().add(privateMessage);
        privateChat.getMessages().add(privateMessage);
        privateChatRepository.save(privateChat);
        privateMessageRepository.save(privateMessage);

        return privateMessage;
    }

    public boolean isUserExistsInChat(User user, PrivateChat privateChat) {
        for (PrivateChatAccount member : privateChat.getMembers()) {
            if(member.getUser().getId().equals(user.getId())) {
                return true;
            }
        }

        return false;
    }

    public void ensureUserPermissionToSendMessageInChat(User user, PrivateChat privateChat) {
        if(!isUserExistsInChat(user, privateChat)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User with id " + user.getId() + " can't send messages to other people's chat");
        }
    }

    public User getSecondUserOfChat(User firstUser, PrivateChat privateChat) throws Exception {
        if(!isUserExistsInChat(firstUser, privateChat)) {
            throw new Exception("Searching of the second user of chat was failed because the first user doesn't exist in chat");
        }

        User secondUser = new User();
        for (PrivateChatAccount member : privateChat.getMembers()) {
            if(!member.getUser().getId().equals(firstUser.getId())) secondUser = member.getUser();
        }

        return secondUser;
    }
}
