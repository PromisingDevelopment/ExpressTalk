package expresstalk.dev.backend.dto;

import jakarta.validation.constraints.Pattern;

public record AddUserToGroupChatDto(
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "Chat id must be a type of UUID")
        String chatId,

        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "Member's id must be a type of UUID")
        String memberId
) {
}
