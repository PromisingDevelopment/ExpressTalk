package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.SendChatMessageDto;
import expresstalk.dev.backend.entity.GroupChat;
import expresstalk.dev.backend.entity.GroupChatAccount;
import expresstalk.dev.backend.entity.GroupMessage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.GroupChatRole;
import expresstalk.dev.backend.exception.ChatIsNotFoundException;
import expresstalk.dev.backend.exception.UserAbsentInChatException;
import expresstalk.dev.backend.exception.UserIsNotAdminException;
import expresstalk.dev.backend.repository.GroupChatAccountRepository;
import expresstalk.dev.backend.repository.GroupChatRepository;
import expresstalk.dev.backend.repository.GroupMessageRepository;
import expresstalk.dev.backend.repository.UserRepository;
import expresstalk.dev.backend.test_utils.TestValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupChatServiceTest {
    @Mock
    private AccountService accountService;
    @Mock
    private GroupChatAccountRepository groupChatAccountRepository;
    @Mock
    private GroupChatRepository groupChatRepository;
    @Mock
    private GroupMessageRepository groupMessageRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private GroupChatService groupChatService;

    @Test
    void shouldGetChat() {
        UUID id = UUID.randomUUID();

        when(groupChatRepository.findById(id)).thenReturn(Optional.of(new GroupChat()));

        assertDoesNotThrow(() -> groupChatService.getChat(id));
    }

    @Test
    void shouldNotGetChat() {
        UUID id = UUID.randomUUID();

        when(groupChatRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ChatIsNotFoundException.class, () -> groupChatService.getChat(id));
    }

    @Test
    void shouldAddMemberToChat() {
        User admin = TestValues.getUser();
        User member = TestValues.getUser();
        GroupChat groupChat = new GroupChat();
        GroupChatAccount adminAccount = new GroupChatAccount();
        adminAccount.setGroupChatRole(GroupChatRole.ADMIN);

        when(accountService.getGroupChatAccount(member, groupChat)).thenReturn(null);
        when(accountService.getGroupChatAccount(admin, groupChat)).thenReturn(adminAccount);

        assertDoesNotThrow(() -> groupChatService.addMemberToChat(admin, member, groupChat));
        verify(groupChatAccountRepository).save(any(GroupChatAccount.class));
        verify(groupChatRepository).save(groupChat);
        verify(userRepository).save(member);
    }

    @Test
    void shouldNotAddMemberToChat() {
        User admin = TestValues.getUser();
        User member = TestValues.getUser();
        GroupChat groupChat = new GroupChat();
        GroupChatAccount memberAccount = new GroupChatAccount();

        when(accountService.getGroupChatAccount(member, groupChat)).thenReturn(memberAccount);
        assertThrows(ResponseStatusException.class,
                () -> groupChatService.addMemberToChat(admin, member, groupChat));

        when(accountService.getGroupChatAccount(member, groupChat)).thenReturn(null);
        when(accountService.getGroupChatAccount(admin, groupChat)).thenReturn(new GroupChatAccount());
        assertThrows(UserIsNotAdminException.class,
                () -> groupChatService.addMemberToChat(admin, member, groupChat));
    }

    @Test
    void shouldRemoveMemberFromChat() {
        User admin = TestValues.getUser();
        User member = TestValues.getUser();
        GroupChat groupChat = new GroupChat();
        GroupChatAccount memberAccount = new GroupChatAccount();
        GroupChatAccount adminAccount = new GroupChatAccount();
        adminAccount.setGroupChatRole(GroupChatRole.ADMIN);
        groupChat.setId(UUID.randomUUID());
        memberAccount.setId(UUID.randomUUID());
        adminAccount.setId(UUID.randomUUID());

        groupChat.getMembers().add(memberAccount);
        groupChat.getMembers().add(adminAccount);
        admin.getGroupChatAccounts().add(adminAccount);
        member.getGroupChatAccounts().add(memberAccount);
        adminAccount.setUser(admin);
        adminAccount.setGroupChat(groupChat);
        memberAccount.setUser(member);
        memberAccount.setGroupChat(groupChat);

        when(accountService.getGroupChatAccount(member, groupChat)).thenReturn(memberAccount);
        when(accountService.getGroupChatAccount(admin, groupChat)).thenReturn(adminAccount);

        assertDoesNotThrow(() -> groupChatService.removeMemberFromChat(admin, member, groupChat));
        verify(groupChatAccountRepository).delete(memberAccount);
        verify(groupChatRepository).save(groupChat);
        verify(userRepository).save(member);
    }

    @Test
    void shouldNotRemoveMemberFromChat() {
        User admin = TestValues.getUser();
        User member = TestValues.getUser();
        GroupChat groupChat = new GroupChat();

        when(accountService.getGroupChatAccount(any(User.class), any(GroupChat.class)))
                .thenReturn(null);
        assertThrows(UserAbsentInChatException.class, () -> groupChatService.removeMemberFromChat(admin, member, groupChat));

        when(accountService.getGroupChatAccount(any(User.class), any(GroupChat.class)))
                .thenReturn(new GroupChatAccount());
        assertThrows(UserIsNotAdminException.class, () -> groupChatService.removeMemberFromChat(admin, member, groupChat));
    }

    @Test
    void shouldCreateChat() {
        assertDoesNotThrow(() -> groupChatService.createChat(new User(), "Work"));
        verify(groupChatRepository).save(any(GroupChat.class));
        verify(groupChatAccountRepository).save(any(GroupChatAccount.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldSetRole() {
        User admin = TestValues.getUser();
        User member = TestValues.getUser();
        GroupChat groupChat = new GroupChat();
        GroupChatAccount memberAccount = new GroupChatAccount();
        GroupChatAccount adminAccount = new GroupChatAccount();
        adminAccount.setGroupChatRole(GroupChatRole.ADMIN);

        when(accountService.getGroupChatAccount(member, groupChat)).thenReturn(memberAccount);
        when(accountService.getGroupChatAccount(admin, groupChat)).thenReturn(adminAccount);

        assertDoesNotThrow(() -> groupChatService.setRole(admin, member, GroupChatRole.ADMIN, groupChat));
        assertEquals(memberAccount.getGroupChatRole(), GroupChatRole.ADMIN);
        verify(groupChatRepository).save(groupChat);
        verify(groupChatAccountRepository).save(memberAccount);
        verify(userRepository).save(member);

        assertDoesNotThrow(() -> groupChatService.setRole(admin, member, GroupChatRole.MEMBER, groupChat));
        assertEquals(memberAccount.getGroupChatRole(), GroupChatRole.MEMBER);
    }

    @Test
    void shouldNotSetRole() {
        User admin = TestValues.getUser();
        User member = TestValues.getUser();
        GroupChat groupChat = new GroupChat();

        when(accountService.getGroupChatAccount(any(User.class), any(GroupChat.class)))
                .thenReturn(null);
        assertThrows(UserAbsentInChatException.class, () -> groupChatService.setRole(admin, member, GroupChatRole.ADMIN, groupChat));

        when(accountService.getGroupChatAccount(any(User.class), any(GroupChat.class)))
                .thenReturn(new GroupChatAccount());
        assertThrows(UserIsNotAdminException.class, () -> groupChatService.setRole(admin, member, GroupChatRole.ADMIN, groupChat));
    }

    @Test
    void shouldVerifyAndGetGroupChatAccount() {
        User user = TestValues.getUser();
        GroupChat groupChat = new GroupChat();

        when(accountService.getGroupChatAccount(user, groupChat)).thenReturn(new GroupChatAccount());
        assertDoesNotThrow(() -> groupChatService.verifyAndGetGroupChatAccount(user, groupChat));
    }

    @Test
    void shouldNotVerifyAndGetGroupChatAccount() {
        User user = TestValues.getUser();
        GroupChat groupChat = new GroupChat();

        when(accountService.getGroupChatAccount(user, groupChat)).thenReturn(null);
        assertThrows(UserAbsentInChatException.class, () -> groupChatService.verifyAndGetGroupChatAccount(user, groupChat));
    }

    @Test
    void shouldSaveMessage() {
        UUID chatId = UUID.randomUUID();
        SendChatMessageDto sendChatMessageDto = new SendChatMessageDto(
                chatId.toString(),
                TestValues.getSentence(),
                TestValues.getCreatedAt()
                );

        when(groupChatRepository.findById(chatId)).thenReturn(Optional.of(new GroupChat()));
        when(accountService.getGroupChatAccount(any(User.class), any(GroupChat.class)))
                .thenReturn(new GroupChatAccount());

        assertDoesNotThrow(() -> groupChatService.saveMessage(new User(), sendChatMessageDto));
        verify(groupChatRepository).save(any(GroupChat.class));
        verify(groupChatAccountRepository).save(any(GroupChatAccount.class));
        verify(groupMessageRepository).save(any(GroupMessage.class));
    }

    @Test
    void shouldNotSaveMessage() {
        UUID chatId = UUID.randomUUID();
        SendChatMessageDto sendChatMessageDto = new SendChatMessageDto(
                chatId.toString(),
                TestValues.getSentence(),
                TestValues.getCreatedAt()
        );

        when(groupChatRepository.findById(chatId)).thenReturn(Optional.empty());
        assertThrows(ChatIsNotFoundException.class, () -> groupChatService.saveMessage(new User(), sendChatMessageDto));

        when(groupChatRepository.findById(chatId)).thenReturn(Optional.of(new GroupChat()));
        when(accountService.getGroupChatAccount(any(User.class), any(GroupChat.class)))
                .thenReturn(null);
        assertThrows(UserAbsentInChatException.class, () -> groupChatService.saveMessage(new User(), sendChatMessageDto));
    }
}