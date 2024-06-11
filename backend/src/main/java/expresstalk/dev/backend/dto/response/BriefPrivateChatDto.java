package expresstalk.dev.backend.dto.response;

import java.util.UUID;

public record BriefPrivateChatDto(
        UUID id,
        UUID receiverId,
        String receiverLogin,
        String lastMessage
) {
}
