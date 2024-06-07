package expresstalk.dev.backend.dto;

import java.util.UUID;

public record GroupChatClientDto(
        UUID id,
        String name,
        String lastMessage
) {
}
