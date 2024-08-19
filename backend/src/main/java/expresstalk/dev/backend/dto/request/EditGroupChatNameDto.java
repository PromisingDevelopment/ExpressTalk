package expresstalk.dev.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EditGroupChatNameDto(
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "Chat id must be a type of UUID")
        String chatId,

        @NotBlank(message = "Name can't be blank")
        String groupName
) {
}
