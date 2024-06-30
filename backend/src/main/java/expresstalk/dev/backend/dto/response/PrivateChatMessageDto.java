package expresstalk.dev.backend.dto.response;

import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

public record PrivateChatMessageDto(
        Boolean isSystemMessage,
        Date createdAt,
        String content,
        @Nullable
        String senderLogin,
        @Nullable
        UUID senderId
) { }
