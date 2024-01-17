package expresstalk.dev.backend.dto;

import java.util.Date;

public record ClientPrivateChatMessageDto(
        String senderLogin,
        String content,
        Date createdAt
) { }
