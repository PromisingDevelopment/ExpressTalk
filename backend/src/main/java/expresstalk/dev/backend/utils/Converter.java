package expresstalk.dev.backend.utils;

import java.util.UUID;

public class Converter {
    public static UUID convertStringToUUID(String strUUID) throws Exception {
        boolean isUUID = RegexChecker.isMatches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}", strUUID);

        if(isUUID) {
            return UUID.fromString(strUUID);
        }

        throw new Exception("Provided string is not UUID");
    }
}
