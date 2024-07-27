package expresstalk.dev.backend.test_utils;

import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.PrivateChatAccount;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;

import java.util.UUID;

public class TestValues {
    public static int userCounter = 0;

    public static String getEmailCode() {
        return "63de93";
    }

    public static String getPassword() {
        return "money_trees123";
    }

    public static User getUser() {
        userCounter++;
        User user = new User(
                "Mike" + userCounter,
                "mike1997" + userCounter,
                "mike1997" + userCounter + "@gmail.com",
                "$2a$10$Bmlw6xdRNY5J15uwPfIfueuxiN2NCIr3dfZtl4FJsTvlq0UnZ5xyS");
        user.setStatus(UserStatus.ONLINE);
        user.setId(UUID.fromString("cdf73316-904c-4250-b1c5-7c8190f8f7ff"));

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

//    private static PrivateChatAccount getPrivateChatAccount
//
//    private static PrivateChat getPrivateChat(User user) {
//        PrivateChatAccount privateChatAccount = new PrivateChatAccount()
//    }
}
