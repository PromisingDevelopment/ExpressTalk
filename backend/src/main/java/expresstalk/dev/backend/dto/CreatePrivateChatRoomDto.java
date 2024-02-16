package expresstalk.dev.backend.dto;

import jakarta.validation.constraints.Pattern;

public record CreatePrivateChatRoomDto(
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "Second user's id must be a type of UUID")
        String secondMemberId
) {
}
