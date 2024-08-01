package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.entity.AttachedFile;

import java.util.Date;
import java.util.UUID;

public record PrivateChatMessageDto(
        Date createdAt,
        String content,
        String senderLogin,
        UUID senderId,
        AttachedFile attachedFile
) {
}
