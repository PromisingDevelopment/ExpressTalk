package expresstalk.dev.backend.dto.response;

import java.util.Date;
import java.util.UUID;

public record MessageDto(
        UUID messageId,
        Date createdAt,
        String content
) {
}
