package expresstalk.dev.backend.dto;

import expresstalk.dev.backend.entity.GroupChat;

import java.util.List;
import java.util.UUID;

public record GetUserChatsDto(
        UUID id,
        List<PrivateChatClientDto> privateChats,
        List<GroupChat> groupChats
) {
}
