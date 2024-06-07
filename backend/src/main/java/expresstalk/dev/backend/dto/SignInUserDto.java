package expresstalk.dev.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInUserDto(
        // This field can contain whether user's login or phone number
        @NotBlank(message = "Login field can't be blank.")
        String login,

        @NotBlank(message = "Password field can't be blank.")
        String password
) {
}