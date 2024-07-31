package expresstalk.dev.backend.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageUtils {
    public static void validateImage(MultipartFile imageFile) throws Exception {
        List<String> acceptedMimeTypes = Arrays.asList(
                "image/jpeg",
                "image/png",
                "image/gif",
                "image/bmp",
                "image/webp"
        );

        String contentType = imageFile.getContentType();
        if (contentType == null || !acceptedMimeTypes.contains(contentType)) {
            throw new Exception("Invalid file. Only images are allowed");
        }
    }
}
