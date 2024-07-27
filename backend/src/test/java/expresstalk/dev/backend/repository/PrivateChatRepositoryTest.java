package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.PrivateChatAccount;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.test_utils.TestValues;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PrivateChatRepositoryTest {
    @Autowired
    private PrivateChatRepository privateChatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivateChatAccountRepository privateChatAccountRepository;

    @AfterEach
    void tearDown() {
        privateChatRepository.deleteAll();
        userRepository.deleteAll();
        privateChatAccountRepository.deleteAll();
    }

    @Test
    void shouldFindPrivateChatBetween() {
        User user1 = TestValues.getUser();
        User user2 = TestValues.getUser();
        user1.setId(null);
        user2.setId(null);
        PrivateChat privateChat = new PrivateChat();
        PrivateChatAccount account1 = new PrivateChatAccount();
        PrivateChatAccount account2 = new PrivateChatAccount();

        account1.setPrivateChat(privateChat);
        account2.setPrivateChat(privateChat);
        account1.setUser(user1);
        account2.setUser(user2);
        privateChat.getMembers().add(account1);
        privateChat.getMembers().add(account2);
        user1.getPrivateChatAccounts().add(account1);
        user2.getPrivateChatAccounts().add(account2);

        privateChatRepository.save(privateChat);
        userRepository.saveAll(Arrays.asList(user1, user2));
        privateChatAccountRepository.saveAll(Arrays.asList(account1, account2));

        PrivateChat result = privateChatRepository.findPrivateChatBetween(user1, user2);
        assertEquals(result.getId(), privateChat.getId());
    }
}