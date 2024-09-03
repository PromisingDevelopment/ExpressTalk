package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.entity.GroupChatAccount;
import expresstalk.dev.backend.entity.Message;

import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

public record GetGroupChatDto(
        UUID id,
        String name,
        TreeSet<Message> messages,
        List<GroupChatAccount> members
) {
}
