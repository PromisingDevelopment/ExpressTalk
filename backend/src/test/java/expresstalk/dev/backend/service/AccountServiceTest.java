package expresstalk.dev.backend.service;

import expresstalk.dev.backend.entity.*;
import expresstalk.dev.backend.enums.GroupChatRole;
import expresstalk.dev.backend.test_utils.TestValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }

    @Test
    void shouldGetPrivateChatAccount() {
        User user1 = TestValues.getUser();
        User user2 = TestValues.getUser();
        PrivateChat privateChat = new PrivateChat();
        PrivateChatAccount account1 = new PrivateChatAccount();
        PrivateChatAccount account2 = new PrivateChatAccount();

        account1.setUser(user1);
        account1.setPrivateChat(privateChat);
        account2.setUser(user2);
        account2.setPrivateChat(privateChat);
        privateChat.getMembers().add(account1);
        privateChat.getMembers().add(account2);
        user1.getPrivateChatAccounts().add(account1);
        user2.getPrivateChatAccounts().add(account2);

        PrivateChatAccount result = assertDoesNotThrow(() -> accountService.getPrivateChatAccount(user1, privateChat));
        assertEquals(result.getUser().getId(), account1.getUser().getId());
        result = assertDoesNotThrow(() -> accountService.getPrivateChatAccount(user2, privateChat));
        assertEquals(result.getUser().getId(), account2.getUser().getId());
    }

    @Test
    void shouldNotGetPrivateChatAccount() {
        User user = TestValues.getUser();
        PrivateChat privateChat = new PrivateChat();

        assertThrows(ResponseStatusException.class, () -> accountService.getPrivateChatAccount(user, privateChat));
    }

    @Test
    void getGroupChatAccount() {
        User user = TestValues.getUser();
        GroupChat groupChat = new GroupChat("Work");
        GroupChatAccount account = new GroupChatAccount(GroupChatRole.ADMIN, groupChat, user);

        groupChat.getMembers().add(account);
        user.getGroupChatAccounts().add(account);

        GroupChatAccount result = assertDoesNotThrow(() -> accountService.getGroupChatAccount(user, groupChat));
        assertEquals(result.getUser().getId(), account.getUser().getId());
    }

    @Test
    void getNotGroupChatAccount() {
        User user = TestValues.getUser();
        GroupChat groupChat = new GroupChat("Work");

        assertThrows(ResponseStatusException.class, () -> accountService.getGroupChatAccount(user, groupChat));
    }
}