package expresstalk.dev.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SendFileDto(
        @NotBlank(message = "file name can't be blank")
        String name,

        @NotBlank(message = "file type can't be blank")
        String type,

        @NotBlank(message = "file data can't be blank")
        byte[] data
) {

}
