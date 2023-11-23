package expresstalk.dev.backend.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpUserDto(
        @NotBlank(message = "Name field can't be blank.")
        String name,

        @Size(min = 4, max = 20, message = "Login must be between 4 and 20 characters long.")

        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Invalid login. It should contain only English letters and numbers.")
        @NotBlank(message = "Login field can't be blank.")
        String login,

        @Email(message = "Invalid email. Please provide correct email address.")
        @NotBlank(message = "Email field can't be blank.")
        String email,

        @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters long.")
        @NotBlank(message = "Password field can't be blank.")
        String password
) {
}
