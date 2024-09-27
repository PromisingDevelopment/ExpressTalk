package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.entity.AttachedFile;

import java.util.UUID;

public record PrivateMessageDetailsDto(
        AttachedFile attachedFile,
        String senderLogin,
        UUID senderId
) {
}
