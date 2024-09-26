package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.entity.AttachedFile;

import java.util.Date;
import java.util.UUID;

public record PrivateChatMessageDto(
        UUID chatId,
        Date createdAt,
        String content,
        AttachedFile attachedFile,
        String senderLogin,
        UUID senderId
) {
}
