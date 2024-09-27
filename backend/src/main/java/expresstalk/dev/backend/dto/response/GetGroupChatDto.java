package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.entity.GroupChatAccount;

import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

public record GetGroupChatDto(
        UUID id,
        String name,
        TreeSet<GroupMessageDto> messages,
        List<GroupChatAccount> members
) {
}
