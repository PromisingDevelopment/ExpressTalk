package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.enums.GroupChatRole;
import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

public record GroupChatMessageDto(
        Boolean isSystemMessage,
        Date createdAt,
        String content,
        @Nullable
        String senderLogin,
        @Nullable
        GroupChatRole senderRole,
        @Nullable
        UUID senderId
) {
        public GroupChatMessageDto(boolean isSystemMessage, Date createdAt, String content) {
                this(
                        isSystemMessage,
                        createdAt,
                        content,
                        null,
                        null,
                        null
                );
        }
}
