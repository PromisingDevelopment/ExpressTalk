package expresstalk.dev.backend.dto.response;

import expresstalk.dev.backend.dto.response.BriefGroupChatDto;
import expresstalk.dev.backend.dto.response.BriefPrivateChatDto;

import java.util.List;

public record GetUserChatsDto(
        List<BriefPrivateChatDto> privateChats,
        List<BriefGroupChatDto> groupChats
) {
}
