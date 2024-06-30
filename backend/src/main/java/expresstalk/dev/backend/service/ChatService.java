package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.GetUserChatsDto;
import expresstalk.dev.backend.dto.response.BriefGroupChatDto;
import expresstalk.dev.backend.dto.response.BriefPrivateChatDto;
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

    public GetUserChatsDto getChats(User user) {
        List<BriefPrivateChatDto> briefPrivateChatDtos = new ArrayList<>();
        for(PrivateChat privateChat : user.getPrivateChats()) {
            User secondUser = new User();
            try {
                secondUser = privateChatService.getSecondUserOfChat(user, privateChat);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }

            List<PrivateChatMessage> messages = privateChat.getMessages();
            String lastMessage = messages.size() == 0 ? "" : messages.getLast().getContent();

            briefPrivateChatDtos.add(
                    new BriefPrivateChatDto(
                            privateChat.getId(),
                            secondUser.getId(),
                            secondUser.getLogin(),
                            lastMessage
                    )
            );
        }

        List<BriefGroupChatDto> briefGroupChatDtos = new ArrayList<>();
        for(GroupChat groupChat : user.getGroupChats()) {
            List<GroupChatMessage> messages = groupChat.getMessages();
            String lastMessage = messages.size() == 0 ? "" : messages.getLast().getContent();

            briefGroupChatDtos.add(
                    new BriefGroupChatDto(
                            groupChat.getId(),
                            groupChat.getName(),
                            lastMessage
                    )
            );
        }

        GetUserChatsDto getUserChatsDto = new GetUserChatsDto(briefPrivateChatDtos, briefGroupChatDtos);

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
