package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.entity.AttachedFile;
import expresstalk.dev.backend.enums.GroupChatRole;

import java.util.Date;
import java.util.UUID;

public record GroupChatMessageDto(
        UUID chatId,
        Date createdAt,
        String content,
        AttachedFile attachedFile,
        String senderLogin,
        UUID senderId,
        GroupChatRole senderRole
) {
}

