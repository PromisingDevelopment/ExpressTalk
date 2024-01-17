package expresstalk.dev.backend.dto;

import expresstalk.dev.backend.entity.GroupChat;

import java.util.List;

public record GetUserChatsDto(
        List<PrivateChatClientDto> privateChats,
        List<GroupChat> groupChats
) {
}
