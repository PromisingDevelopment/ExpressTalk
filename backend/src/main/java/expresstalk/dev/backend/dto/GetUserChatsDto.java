package expresstalk.dev.backend.dto;

import expresstalk.dev.backend.entity.GroupChat;
import expresstalk.dev.backend.entity.PrivateChat;

import java.util.List;
import java.util.UUID;

public record GetUserChatsDto(
        UUID id,
        List<PrivateChat> privateChats,
        List<GroupChat> groupChats
) {
}
