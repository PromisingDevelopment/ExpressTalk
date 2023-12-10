package expresstalk.dev.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerificationDto(
        @Email(message = "Please provide correct email address,")
        @NotBlank(message = "Email can't be blank.")
        String email,

        @NotBlank(message = "Code field can't be blank.")
        String code
) {
}
