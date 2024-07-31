package expresstalk.dev.backend.test_utils;

import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TestValues {
    public static int userCounter = 0;
    private static final String[] SUBJECTS = {
            "The cat", "A dog", "The bird", "A child", "The teacher",
            "The student", "The programmer", "An artist", "The scientist", "The musician"
    };

    private static final String[] VERBS = {
            "jumps over", "runs around", "flies above", "sits on", "writes",
            "paints", "studies", "plays with", "sings to", "builds"
    };

    private static final String[] OBJECTS = {
            "the fence", "the park", "the sky", "the chair", "a book",
            "a canvas", "a computer", "a toy", "a song", "a robot"
    };

    private static final String[] ADVERBS = {
            "quickly", "slowly", "gracefully", "awkwardly", "happily",
            "sadly", "angrily", "calmly", "silently", "loudly"
    };
    public static String getEmailCode() {
        int codeLength = 6;
        String hexChars = "0123456789abcdef";
        StringBuilder randomCode = new StringBuilder(codeLength);
        Random random = new Random();

        for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(hexChars.length());
            randomCode.append(hexChars.charAt(randomIndex));
        }

        return randomCode.toString();
    }

    public static String getPassword() {
        int length = 13;
        String lowerCaseChars = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String underscore = "_";
        String allChars = lowerCaseChars + digits + underscore;
        StringBuilder randomPassword = new StringBuilder(length);
        Random random = new Random();
        randomPassword.append(lowerCaseChars.charAt(random.nextInt(lowerCaseChars.length())));
        for (int i = 1; i < length; i++) {
            int randomIndex = random.nextInt(allChars.length());
            randomPassword.append(allChars.charAt(randomIndex));
        }

        return randomPassword.toString();
    }

    public static String getSentence() {
        Random random = new Random();
        String subject = SUBJECTS[random.nextInt(SUBJECTS.length)];
        String verb = VERBS[random.nextInt(VERBS.length)];
        String object = OBJECTS[random.nextInt(OBJECTS.length)];
        String adverb = ADVERBS[random.nextInt(ADVERBS.length)];
        String randomSentence = subject + " " + verb + " " + object + " " + adverb + ".";

        return randomSentence;
    }

    public static String getWord() {
        Random random = new Random();
        String subject = SUBJECTS[random.nextInt(SUBJECTS.length)];

        return subject;
    }

    public static String getCreatedAt() {
        LocalDate startDate = LocalDate.of(2010, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 7, 31);

        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);

        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        int randomHour = ThreadLocalRandom.current().nextInt(0, 24);
        int randomMinute = ThreadLocalRandom.current().nextInt(0, 60);
        int randomSecond = ThreadLocalRandom.current().nextInt(0, 60);

        LocalDateTime randomDateTime = randomDate.atTime(randomHour, randomMinute, randomSecond);
        Instant randomInstant = randomDateTime.toInstant(ZoneOffset.UTC);
        long randomTimestamp = randomInstant.toEpochMilli();

        return String.valueOf(randomTimestamp);
    }

    public static User getUser() {
        userCounter++;
        User user = new User(
                "Mike" + userCounter,
                "mike1997" + userCounter,
                "mike1997" + userCounter + "@gmail.com",
                "$2a$10$Bmlw6xdRNY5J15uwPfIfueuxiN2NCIr3dfZtl4FJsTvlq0UnZ5xyS");
        user.setStatus(UserStatus.ONLINE);
        user.setId(UUID.randomUUID());

        return user;
    }

    public static User getSameUser() {
        User user = new User(
                "Mike" + userCounter,
                "mike1997" + userCounter,
                "mike1997" + userCounter + "@gmail.com",
                "$2a$10$Bmlw6xdRNY5J15uwPfIfueuxiN2NCIr3dfZtl4FJsTvlq0UnZ5xyS");
        user.setStatus(UserStatus.ONLINE);

        return user;
    }
}
