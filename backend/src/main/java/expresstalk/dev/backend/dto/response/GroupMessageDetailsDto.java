package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.entity.AttachedFile;
import expresstalk.dev.backend.enums.GroupChatRole;

import java.util.UUID;

public record GroupMessageDetailsDto(
        AttachedFile attachedFile,
        String senderLogin,
        UUID senderId,
        GroupChatRole senderRole
) {
}
