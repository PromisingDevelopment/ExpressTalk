package expresstalk.dev.backend.dto.request;

import expresstalk.dev.backend.enums.GroupChatRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record SetUserRoleInGroupChatDto(
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "Chat id must be a type of UUID")
        String chatId,

        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "User to give role's id must be a type of UUID")
        String userToGiveRoleId,

        GroupChatRole groupChatRole
) {
}
