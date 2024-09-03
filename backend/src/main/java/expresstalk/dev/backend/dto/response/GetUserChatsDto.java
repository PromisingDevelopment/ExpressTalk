package expresstalk.dev.backend.dto.response;

import java.util.List;

public record GetUserChatsDto(
        List<BriefPrivateChatDto> privateChats,
        List<BriefGroupChatDto> groupChats
) {
}
