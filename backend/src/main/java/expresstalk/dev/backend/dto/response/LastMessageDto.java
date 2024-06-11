package expresstalk.dev.backend.dto.response;

import java.util.UUID;

public record LastMessageDto(
    UUID chatId,
    String lastMessage
) { }
