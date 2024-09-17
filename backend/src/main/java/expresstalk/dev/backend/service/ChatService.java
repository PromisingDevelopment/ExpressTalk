package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.response.GetUserChatsDto;
import expresstalk.dev.backend.dto.response.BriefGroupChatDto;
import expresstalk.dev.backend.dto.response.BriefPrivateChatDto;
import expresstalk.dev.backend.entity.*;
import expresstalk.dev.backend.exception.InternalServerErrorException;
import expresstalk.dev.backend.exception.InvalidIdException;
import expresstalk.dev.backend.repository.GroupChatRepository;
import expresstalk.dev.backend.repository.SystemMessageRepository;
import expresstalk.dev.backend.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final GroupChatRepository groupChatRepository;
    private final PrivateChatService privateChatService;
    private final SystemMessageRepository systemMessageRepository;

    public GetUserChatsDto getChats(User user) {
        List<BriefPrivateChatDto> briefPrivateChatDtos = getBriefPrivateChatsDtos(user);
        List<BriefGroupChatDto> briefGroupChatDtos = getBriefGroupChatsDtos(user);
        GetUserChatsDto getUserChatsDto = new GetUserChatsDto(briefPrivateChatDtos, briefGroupChatDtos);

        return getUserChatsDto;
    }

    public UUID verifyAndGetChatUUID(String chatStrId) {
        UUID chatId = UUID.randomUUID();
        try {
            chatId = Converter.convertStringToUUID(chatStrId);
        } catch (Exception ex) {
            throw new InvalidIdException("chat");
        }

        return chatId;
    }

    public SystemMessage saveSystemMessage(String message, GroupChat groupChat) {
        SystemMessage systemMessage = new SystemMessage(message, new Date(), groupChat);
        groupChat.getSystemMessages().add(systemMessage);

        systemMessageRepository.save(systemMessage);
        groupChatRepository.save(groupChat);

        return systemMessage;
    }

    private List<BriefGroupChatDto> getBriefGroupChatsDtos(User user) {
        List<BriefGroupChatDto> briefGroupChatDtos = new ArrayList<>();

        for(GroupChatAccount groupAccount : user.getGroupChatAccounts()) {
            GroupChat groupChat = groupAccount.getGroupChat();
            TreeSet<GroupMessage> messages = new TreeSet<>(groupChat.getMessages());
            String lastMessage = messages.isEmpty() ? "" : messages.getLast().getContent();

            briefGroupChatDtos.add(
                    new BriefGroupChatDto(
                            groupChat.getId(),
                            groupChat.getName(),
                            lastMessage
                    )
            );
        }

        return briefGroupChatDtos;
    }

    private List<BriefPrivateChatDto> getBriefPrivateChatsDtos(User user) {
        List<BriefPrivateChatDto> briefPrivateChatDtos = new ArrayList<>();

        for(PrivateChatAccount privateAccount : user.getPrivateChatAccounts()) {
            PrivateChat privateChat = privateAccount.getPrivateChat();
            User secondUser = new User();
            try {
                secondUser = privateChatService.getSecondUserOfChat(user, privateChat);
            } catch (Exception ex) {
                throw new InternalServerErrorException(ex.getMessage());
            }
            TreeSet<PrivateMessage> messages = new TreeSet<>(privateChat.getMessages());
            String lastMessage = messages.isEmpty() ? "" : messages.getLast().getContent();

            briefPrivateChatDtos.add(
                    new BriefPrivateChatDto(
                            privateChat.getId(),
                            secondUser.getId(),
                            secondUser.getLogin(),
                            lastMessage
                    )
            );
        }

        return briefPrivateChatDtos;
    }
}
