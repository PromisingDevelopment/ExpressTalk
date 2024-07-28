package expresstalk.dev.backend.service;

import expresstalk.dev.backend.entity.GroupChat;
import expresstalk.dev.backend.entity.SystemMessage;
import expresstalk.dev.backend.repository.GroupChatRepository;
import expresstalk.dev.backend.repository.SystemMessageRepository;
import expresstalk.dev.backend.test_utils.TestValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
    @Mock
    private GroupChatRepository groupChatRepository;
    @Mock
    private PrivateChatService privateChatService;
    @Mock
    private SystemMessageRepository systemMessageRepository;
    @InjectMocks
    private ChatService chatService;

    @Test
    void shouldVerifyAndGetChatUUID() {
        String testId1 = UUID.randomUUID().toString();
        String testId2 = TestValues.getSentence();
        String testId3 = TestValues.getEmailCode();
        String testId4 = TestValues.getCreatedAt();

        assertDoesNotThrow(() -> chatService.verifyAndGetChatUUID(testId1));
        assertThrows(RuntimeException.class, () -> chatService.verifyAndGetChatUUID(testId2));
        assertThrows(RuntimeException.class, () -> chatService.verifyAndGetChatUUID(testId3));
        assertThrows(RuntimeException.class, () -> chatService.verifyAndGetChatUUID(testId4));
    }

    @Test
    void shouldSaveSystemMessage() {
        String content = TestValues.getSentence();
        GroupChat groupChat = new GroupChat();

        assertDoesNotThrow(() -> chatService.saveSystemMessage(content, groupChat));
        verify(systemMessageRepository).save(any(SystemMessage.class));
        verify(groupChatRepository).save(groupChat);
    }
}