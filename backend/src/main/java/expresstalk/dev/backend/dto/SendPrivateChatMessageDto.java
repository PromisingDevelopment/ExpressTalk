package expresstalk.dev.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SendPrivateChatMessageDto(
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "chat id must be a type of UUID")
        String chatId,

        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "sender id must be a type of UUID")
        String senderId,

        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", message = "receiver id must be a type of UUID")
        String receiverId,

        @NotBlank(message = "content can't be blank")
        String content,

        @NotBlank(message = "created at date can't be blank")
        String createdAt
) { }
