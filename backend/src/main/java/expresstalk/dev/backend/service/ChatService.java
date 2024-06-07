package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.GetUserChatsDto;
import expresstalk.dev.backend.dto.GroupChatClientDto;
import expresstalk.dev.backend.dto.PrivateChatClientDto;
import expresstalk.dev.backend.entity.*;
import expresstalk.dev.backend.utils.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    private final UserService userService;
    private final PrivateChatService privateChatService;

    public ChatService(UserService userService, PrivateChatService privateChatService) {
        this.userService = userService;
        this.privateChatService = privateChatService;
    }

    public GetUserChatsDto getChats(UUID userId) {
        User user = new User();
        try {
            user = userService.findById(userId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User id stored in session doesn't storage real user's id");
        }

        List<PrivateChatClientDto> privateChatClientDtos = new ArrayList<>();
        for(PrivateChat privateChat : user.getPrivateChats()) {
            User secondUser = new User();
            try {
                secondUser = privateChatService.getSecondUserOfChat(user.getId(), privateChat);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }

            List<PrivateChatMessage> messages = privateChat.getMessages();
            String lastMessage = messages.size() == 0 ? "" : messages.getLast().getContent();

            privateChatClientDtos.add(
                    new PrivateChatClientDto(
                            privateChat.getId(),
                            secondUser.getLogin(),
                            lastMessage
                    )
            );
        }

        List<GroupChatClientDto> groupChatClientDtos = new ArrayList<>();
        for(GroupChat groupChat : user.getGroupChats()) {
            List<GroupChatMessage> messages = groupChat.getMessages();
            String lastMessage = messages.size() == 0 ? "" : messages.getLast().getContent();

            groupChatClientDtos.add(
                    new GroupChatClientDto(
                            groupChat.getId(),
                            groupChat.getName(),
                            lastMessage
                    )
            );
        }

        GetUserChatsDto getUserChatsDto = new GetUserChatsDto(privateChatClientDtos, groupChatClientDtos);

        return getUserChatsDto;
    }

    public UUID checkAndGetChatUUID(String chatStrId) {
        UUID chatId = UUID.randomUUID();
        try {
            chatId = Converter.convertStringToUUID(chatStrId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided chat id in the path is not UUID");
        }

        return chatId;
    }
}
