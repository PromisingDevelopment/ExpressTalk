package expresstalk.dev.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {
    public static boolean isMatches(String regex, String stringToCheck) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringToCheck);

        return matcher.matches();
    }
}
