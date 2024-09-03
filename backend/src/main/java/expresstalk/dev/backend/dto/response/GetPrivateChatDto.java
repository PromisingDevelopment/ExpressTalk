package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.entity.Message;
import expresstalk.dev.backend.entity.PrivateChatAccount;

import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

public record GetPrivateChatDto(
        UUID id,
        TreeSet<Message> messages,
        List<PrivateChatAccount> members
) {
}
