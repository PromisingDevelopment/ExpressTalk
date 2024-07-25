package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.GetUserChatsDto;
import expresstalk.dev.backend.dto.response.BriefGroupChatDto;
import expresstalk.dev.backend.dto.response.BriefPrivateChatDto;
import expresstalk.dev.backend.entity.*;
import expresstalk.dev.backend.repository.GroupChatRepository;
import expresstalk.dev.backend.repository.PrivateChatRepository;
import expresstalk.dev.backend.repository.SystemMessageRepository;
import expresstalk.dev.backend.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final GroupChatRepository groupChatRepository;
    private final PrivateChatRepository privateChatRepository;
    private final PrivateChatService privateChatService;
    private final SystemMessageRepository systemMessageRepository;
    private final AccountService accountService;

    public GetUserChatsDto getChats(User user) {
        List<BriefPrivateChatDto> briefPrivateChatDtos = new ArrayList<>();
        for(PrivateChatAccount privateAccount : user.getPrivateChatAccounts()) {
            PrivateChat privateChat = privateAccount.getPrivateChat();
            User secondUser = new User();
            try {
                secondUser = privateChatService.getSecondUserOfChat(user, privateChat);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
            List<PrivateMessage> messages = privateChat.getMessages();
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
        for(GroupChatAccount groupAccount : user.getGroupChatAccounts()) {
            GroupChat groupChat = groupAccount.getGroupChat();
            List<GroupMessage> messages = groupChat.getMessages();
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

    public SystemMessage saveSystemMessage(String message, GroupChat groupChat) {
        SystemMessage systemMessage = new SystemMessage(message, new Date(), groupChat);

        systemMessageRepository.save(systemMessage);
        groupChatRepository.save(groupChat);

        return systemMessage;
    }
}
