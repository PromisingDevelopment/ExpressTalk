package expresstalk.dev.backend.dto.response;

import java.util.UUID;

public record BriefGroupChatDto(
        UUID id,
        String name,
        String lastMessage
) {
}
