package expresstalk.dev.backend.utils;

import java.util.UUID;

public class Generator {
    public static String generateCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
