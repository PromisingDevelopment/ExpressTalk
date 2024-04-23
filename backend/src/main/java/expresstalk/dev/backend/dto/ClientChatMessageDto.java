package expresstalk.dev.backend.dto;

import java.util.Date;

public record ClientChatMessageDto(
        String senderLogin,
        String content,
        Date createdAt
) { }
