package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.entity.GroupChatAccount;

import java.util.List;
import java.util.UUID;

public record UpdatedMembersDto (
        UUID chatId,
        List<GroupChatAccount> members
) {}
