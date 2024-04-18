package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.SendChatMessageDto;
import expresstalk.dev.backend.entity.*;
import expresstalk.dev.backend.enums.GroupChatRole;
import expresstalk.dev.backend.repository.GroupChatMessageRepository;
import expresstalk.dev.backend.repository.GroupChatRepository;
import expresstalk.dev.backend.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class GroupChatSerivce {
    private final GroupChatRepository groupChatRepository;
    private final GroupChatMessageRepository groupChatMessageRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public GroupChatSerivce(GroupChatRepository groupChatRepository, GroupChatMessageRepository groupChatMessageRepository,
                            UserRepository userRepository, UserService userService) {
        this.groupChatRepository = groupChatRepository;
        this.groupChatMessageRepository = groupChatMessageRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    private boolean isUserExistsInChat(GroupChat groupChat, User user) {
        for(User groupChatUser : groupChat.getMembers()) {
            if(groupChatUser.getId().equals(user.getId())) {
                return true;
            }
        }

        return false;
    }

    private boolean isUserAdminInChat(GroupChat groupChat, User user) {
        for(User groupChatUser : groupChat.getAdmins()) {
            if(groupChatUser.getId().equals(user.getId())) {
                return true;

            }
        }

        return false;
    }

    public GroupChat getChat(UUID chatId) {
        GroupChat groupChat = groupChatRepository.findById(chatId).orElse(null);

        if(groupChat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat with id " + chatId + " wasn't found");
        }

        return groupChat;
    }

    public void addMemberToChat(GroupChat groupChat, User admin, User member) {
        boolean isMemberAlreadyInChat = isUserExistsInChat(groupChat, member);
        boolean isAdmin = isUserAdminInChat(groupChat, admin);

        if(isMemberAlreadyInChat) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The member is already in this chat");
        }

        if(!isAdmin) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can not add new members to chat because user is not admin");
        }

        groupChat.getMembers().add(member);
        member.getGroupChats().add(groupChat);

        groupChatRepository.save(groupChat);
        userRepository.save(member);
    }

    public GroupChat createChat(UUID userId, String groupName) {
        User user = new User();
        try {
            user = userService.findById(userId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User id stored in session doesn't storage real user's id");
        }
        GroupChat groupChat = new GroupChat(groupName);

        groupChat.getMembers().add(user);
        groupChat.getAdmins().add(user);
        user.getGroupChats().add(groupChat);
        user.getAdministratedGroupChats().add(groupChat);

        groupChatRepository.save(groupChat);
        userRepository.save(user);

        return groupChat;
    }

    public void removeMember(GroupChat groupChat, User admin, User member) {
        boolean isMemberInChat = isUserExistsInChat(groupChat, member);
        boolean isAdmin = isUserAdminInChat(groupChat, admin);

        if(!isMemberInChat) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The member is not present in the chat");
        }

        if(!isAdmin) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can not remove members from the chat because user is not admin");
        }

        groupChat.getMembers().remove(member);
        member.getGroupChats().remove(groupChat);

        groupChatRepository.save(groupChat);
        userRepository.save(member);
    }

    public void setRole(GroupChat groupChat, User admin, User member, GroupChatRole role) {
        boolean isMemberInChat = isUserExistsInChat(groupChat, member);
        boolean isAdmin = isUserAdminInChat(groupChat, admin);

        if(!isMemberInChat) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The member is not present in the chat");
        }

        if(!isAdmin) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can not set role for members from the chat because user is not admin");
        }

        if(role.equals(GroupChatRole.ADMIN)) {
            System.out.println("\nADMIN\n");
            boolean isMemberAdmin = isUserAdminInChat(groupChat, member);

            if(isMemberAdmin) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not give admin role to the member. The member is already admin in the chat");
            }

            member.getAdministratedGroupChats().add(groupChat);
            groupChat.getAdmins().add(member);
        }

        if(role.equals(GroupChatRole.MEMBER)) {
            System.out.println("\nMEMBER\n");
            boolean isMemberAdmin = isUserAdminInChat(groupChat, member);

            if(isMemberAdmin) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin can not take off admin rights from another admin");
            }

            groupChat.getAdmins().remove(member);
            member.getAdministratedGroupChats().remove(groupChat);
        }

        groupChatRepository.save(groupChat);
        userRepository.save(member);
    }

    public GroupChat getChat(User user, UUID chatId) {
        GroupChat groupChat = groupChatRepository.findById(chatId).orElse(null);

        if(groupChat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group chat with provided id wasn't found");
        }

        boolean isMemberInChat = isUserExistsInChat(groupChat, user);

        if(!isMemberInChat) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The member is not present in the chat");
        }

        return groupChat;
    }

    public List<User> getOtherUsersOfChat(UUID currentUserId, UUID chatId) {
        GroupChat groupChat = getChat(chatId);
        List<User> users = groupChat.getMembers();
        users.stream().filter((User user)-> !user.getId().equals(currentUserId));

        return users;
    }

    public void ensureUserPermissionToSendMessageInChat(User user, UUID chatId) {
        if(!isUserExistsInChat(getChat(chatId), user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User with id " + user.getId() + " can't send messages to other people's chat");
        }
    }

    public GroupChatMessage saveMessage(SendChatMessageDto sendChatMessageDto, UUID senderId) {
        GroupChat groupChat = getChat(UUID.fromString(sendChatMessageDto.chatId()));

        GroupChatMessage groupChatMessage = new GroupChatMessage(
                senderId,
                sendChatMessageDto.content(),
                new Date(Long.parseLong(sendChatMessageDto.createdAt()))
        );

        groupChat.getMessages().add(groupChatMessage);
        groupChatMessage.setGroupChat(groupChat);
        groupChatRepository.save(groupChat);
        groupChatMessageRepository.save(groupChatMessage);

        return groupChatMessage;
    }
}
