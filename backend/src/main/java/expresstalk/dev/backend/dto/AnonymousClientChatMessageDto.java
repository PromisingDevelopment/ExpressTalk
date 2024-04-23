package expresstalk.dev.backend.dto;

import java.util.Date;

public record AnonymousClientChatMessageDto (
    String content,
    Date createdAt
) {}
