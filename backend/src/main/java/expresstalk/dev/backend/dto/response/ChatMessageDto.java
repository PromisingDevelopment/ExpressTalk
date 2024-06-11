package expresstalk.dev.backend.dto.response;

import java.util.Date;
import java.util.UUID;

public record ChatMessageDto(
        UUID senderId,
        String senderLogin,
        String content,
        Date createdAt
) { }
