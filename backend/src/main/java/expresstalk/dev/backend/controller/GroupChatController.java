package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.request.*;
import expresstalk.dev.backend.dto.response.GetGroupChatDto;
import expresstalk.dev.backend.entity.GroupChat;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.service.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/group_chats")
public class GroupChatController {
    private final UserService userService;
    private final GroupChatService groupChatService;
    private final ChatService chatService;
    private final SessionService sessionService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ResponseBody
    public GroupChat createGroupChatRoom(@RequestBody @Valid CreateGroupChatRoomDto createGroupChatRoomDto, HttpServletRequest request) {
        UUID userId = sessionService.getUserIdFromSession(request);
        User user = userService.findById(userId);
        GroupChat chat = groupChatService.createChat(user, createGroupChatRoomDto.groupName());

        return chat;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Provided chat id in the path is not UUID"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Group chat with provided id wasn't found"),
            @ApiResponse(responseCode = "404", description = "User with provided login doesn't exist in the group chat"),
            @ApiResponse(responseCode = "404", description = "User with provided id doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping ("/{chatStrId}")
    @ResponseBody
    public GetGroupChatDto getGroupChat(@PathVariable String chatStrId, HttpServletRequest request) {
        UUID chatId = chatService.verifyAndGetChatUUID(chatStrId);
        UUID userId = sessionService.getUserIdFromSession(request);
        User user = userService.findById(userId);
        GroupChat groupChat = groupChatService.getChat(chatId);

        groupChatService.ensureUserExistsInChat(user, groupChat);


        GetGroupChatDto getGroupChatDto = new GetGroupChatDto(
                groupChat.getId(),
                groupChat.getName(),
                groupChatService.getGroupMessageDtos(groupChat),
                groupChat.getMembers()
        );

        return getGroupChatDto;
    }
}
