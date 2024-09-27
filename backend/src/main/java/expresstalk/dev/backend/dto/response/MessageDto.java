package expresstalk.dev.backend.dto.response;

import java.util.Date;
import java.util.UUID;

public record MessageDto(
        UUID chatId,
        Date createdAt,
        String content
) {
}
