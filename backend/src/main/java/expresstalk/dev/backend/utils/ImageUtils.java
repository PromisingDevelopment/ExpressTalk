package expresstalk.dev.backend.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.InvalidObjectException;
import java.util.Arrays;
import java.util.List;

public class ImageUtils {
    public static void validateImage(MultipartFile imageFile) throws InvalidObjectException {
        List<String> acceptedMimeTypes = Arrays.asList(
                "image/jpeg",
                "image/png",
                "image/gif",
                "image/bmp",
                "image/webp"
        );

        String contentType = imageFile.getContentType();
        if (contentType == null || !acceptedMimeTypes.contains(contentType)) {
            throw new InvalidObjectException("Invalid file. Only images are allowed");
        }
    }
}
