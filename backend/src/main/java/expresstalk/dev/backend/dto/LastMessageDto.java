package expresstalk.dev.backend.dto;

import java.util.UUID;

public record LastMessageDto(
    UUID chatId,
    String lastMessage
) { }
