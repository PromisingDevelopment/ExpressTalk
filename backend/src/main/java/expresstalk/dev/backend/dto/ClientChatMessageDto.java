package expresstalk.dev.backend.dto;

import java.util.Date;
import java.util.UUID;

public record ClientChatMessageDto(
        UUID senderId,
        String senderLogin,
        String content,
        Date createdAt
) { }
