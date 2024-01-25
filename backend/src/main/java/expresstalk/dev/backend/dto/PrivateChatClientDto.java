package expresstalk.dev.backend.dto;

import java.util.UUID;

public record PrivateChatClientDto(
        UUID id,
        String receiverLogin,
        String lastMessage
) {
}
