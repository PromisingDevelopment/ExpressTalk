package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.entity.AttachedFile;
import expresstalk.dev.backend.enums.GroupChatRole;

import java.util.Date;
import java.util.UUID;

public record GroupChatMessageDto(
        Date createdAt,
        String content,
        String senderLogin,
        GroupChatRole senderRole,
        UUID senderId,
        AttachedFile attachedFile
) {
}

