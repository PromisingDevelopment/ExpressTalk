package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.SendChatMessageDto;
import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.PrivateChatAccount;
import expresstalk.dev.backend.entity.PrivateMessage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.exception.ChatIsNotFoundException;
import expresstalk.dev.backend.exception.UserAbsentInChatException;
import expresstalk.dev.backend.repository.PrivateChatAccountRepository;
import expresstalk.dev.backend.repository.PrivateChatRepository;
import expresstalk.dev.backend.repository.PrivateMessageRepository;
import expresstalk.dev.backend.repository.UserRepository;
import expresstalk.dev.backend.test_utils.TestValues;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrivateChatServiceTest {
    @Mock
    private AccountService accountService;
    @Mock
    private PrivateMessageRepository privateMessageRepository;
    @Mock
    private PrivateChatAccountRepository privateChatAccountRepository;
    @Mock
    private PrivateChatRepository privateChatRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private PrivateChatService privateChatService;

    @Test
    void shouldGetChat() {
        when(privateChatRepository.findById(any(UUID.class))).thenReturn(Optional.of(new PrivateChat()));
        assertNotNull(privateChatService.getChat(UUID.randomUUID()));

        User user1 = TestValues.getUser();
        User user2 = TestValues.getUser();
        when(privateChatRepository.findPrivateChatBetween(user1, user2)).thenReturn(new PrivateChat());
        assertNotNull(privateChatService.getChat(user1, user2));
    }

    @Test
    void shouldNotGetChat() {
        when(privateChatRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ChatIsNotFoundException.class, () -> privateChatService.getChat(UUID.randomUUID()));

        User user1 = TestValues.getUser();
        User user2 = TestValues.getUser();
        when(privateChatRepository.findPrivateChatBetween(user1, user2)).thenReturn(null);
        assertThrows(ChatIsNotFoundException.class, () -> privateChatService.getChat(user1, user2));
    }

    @Test
    void shouldCreatePrivateChat() {
        User user1 = TestValues.getUser();
        User user2 = TestValues.getUser();

        PrivateChat result = assertDoesNotThrow(() -> privateChatService.createPrivateChat(user1, user2));
        verify(privateChatRepository).save(any(PrivateChat.class));
        verify(privateChatAccountRepository).saveAll(anyList());
        verify(userRepository).saveAll(anyList());
        assertNotNull(result);
        assertEquals(result.getMembers().size(), 2);
    }

    @Test
    void shouldNotCreatePrivateChat() {
        User user = TestValues.getUser();
        assertThrows(ResponseStatusException.class, () -> privateChatService.createPrivateChat(user, user));

        User user1 = TestValues.getUser();
        User user2 = TestValues.getUser();
        when(privateChatRepository.findPrivateChatBetween(user1, user2)).thenReturn(new PrivateChat());
        assertThrows(ResponseStatusException.class, () -> privateChatService.createPrivateChat(user1, user2));
    }

    @Test
    void shouldSaveMessage() {
        SendChatMessageDto sendChatMessageDto = new SendChatMessageDto(
                UUID.randomUUID().toString(),
                TestValues.getSentence(),
                TestValues.getCreatedAt()
        );
        User sender = TestValues.getUser();
        User receiver = TestValues.getUser();

        when(privateChatRepository.findById(any(UUID.class))).thenReturn(Optional.of(new PrivateChat()));
        when(accountService.getPrivateChatAccount(
                any(User.class), any(PrivateChat.class))).thenReturn(new PrivateChatAccount());

        assertDoesNotThrow(() -> privateChatService.saveMessage(sendChatMessageDto, sender, receiver));
        verify(privateChatRepository).save(any(PrivateChat.class));
        verify(privateMessageRepository).save(any(PrivateMessage.class));
        verify(privateChatAccountRepository).saveAll(anyList());
    }

    @Test
    void shouldNotSaveMessage() {
        SendChatMessageDto sendChatMessageDto = new SendChatMessageDto(
                UUID.randomUUID().toString(),
                TestValues.getSentence(),
                TestValues.getCreatedAt()
        );
        User sender = TestValues.getUser();
        User receiver = TestValues.getUser();

        when(privateChatRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(ChatIsNotFoundException.class,
                () -> privateChatService.saveMessage(sendChatMessageDto, sender, receiver));

        when(privateChatRepository.findById(any(UUID.class))).thenReturn(Optional.of(new PrivateChat()));
        when(accountService.getPrivateChatAccount(
                any(User.class), any(PrivateChat.class))).thenReturn(null);
        assertThrows(UserAbsentInChatException.class,
                () -> privateChatService.saveMessage(sendChatMessageDto, sender, receiver));
    }

    @Test
    void shouldVerifyUserExistenseInChat() {
        User user = TestValues.getUser();
        PrivateChat privateChat = new PrivateChat();

        when(accountService.getPrivateChatAccount(user, privateChat)).thenReturn(new PrivateChatAccount());

        assertTrue(privateChatService.isUserExistsInChat(user, privateChat));
    }

    @Test
    void shouldNotVerifyUserExistenseInChat() {
        User user = TestValues.getUser();
        PrivateChat privateChat = new PrivateChat();

        when(accountService.getPrivateChatAccount(user, privateChat)).thenReturn(null);

        assertFalse(privateChatService.isUserExistsInChat(user, privateChat));
    }

    @Test
    void shouldGetSecondUserOfChat() {
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

        when(accountService.getPrivateChatAccount(user1, privateChat)).thenReturn(account1);
        when(accountService.getPrivateChatAccount(user2, privateChat)).thenReturn(account2);

        User result = assertDoesNotThrow(() -> privateChatService.getSecondUserOfChat(user1, privateChat));
        assertEquals(user2.getId(), result.getId());
        result = assertDoesNotThrow(() -> privateChatService.getSecondUserOfChat(user2, privateChat));
        assertEquals(user1.getId(), result.getId());
    }

    @Test
    void shouldNotGetSecondUserOfChat() {
        User user1 = TestValues.getUser();
        User user2 = TestValues.getUser();
        PrivateChat privateChat = new PrivateChat();

        when(accountService.getPrivateChatAccount(user1, privateChat)).thenReturn(null);
        when(accountService.getPrivateChatAccount(user2, privateChat)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> privateChatService.getSecondUserOfChat(user1, privateChat));
        assertThrows(ResponseStatusException.class, () -> privateChatService.getSecondUserOfChat(user2, privateChat));
    }
}