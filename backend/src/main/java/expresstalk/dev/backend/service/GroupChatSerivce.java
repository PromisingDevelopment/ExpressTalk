package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.SendChatMessageDto;
import expresstalk.dev.backend.entity.GroupChat;
import expresstalk.dev.backend.entity.GroupChatAccount;
import expresstalk.dev.backend.entity.GroupMessage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.GroupChatRole;
import expresstalk.dev.backend.repository.GroupChatAccountRepository;
import expresstalk.dev.backend.repository.GroupChatRepository;
import expresstalk.dev.backend.repository.GroupMessageRepository;
import expresstalk.dev.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class GroupChatSerivce {
    private final AccountService accountService;
    private final GroupChatAccountRepository groupChatAccountRepository;
    private final GroupChatRepository groupChatRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final UserRepository userRepository;

    private boolean isUserExistsInChat(GroupChat groupChat, User user) {
        for(GroupChatAccount member : groupChat.getMembers()) {
            if(member.getUser().getId().equals(user.getId())) {
                return true;
            }
        }

        return false;
    }

//    public GroupChatRole getRole(GroupChat groupChat, User user) {
//        return isAdmin(groupChat, user) ? GroupChatRole.ADMIN : GroupChatRole.MEMBER;
//    }

    public GroupChat getChat(UUID chatId) {
        GroupChat groupChat = groupChatRepository.findById(chatId).orElse(null);

        if(groupChat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat with id " + chatId + " wasn't found");
        }

        return groupChat;
    }

    public void addMemberToChat(GroupChat groupChat, User admin, User member) {
        boolean isMemberAlreadyInChat = isUserExistsInChat(groupChat, member);
        GroupChatAccount adminAccount = accountService.getGroupChatAccount(admin, groupChat);

        if(isMemberAlreadyInChat) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The member is already in this chat");
        }
        if(!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can not add new members to chat because user is not admin");
        }

        GroupChatAccount memberAccount = new GroupChatAccount(groupChat, member);

        groupChat.getMembers().add(memberAccount);

        groupChatAccountRepository.save(memberAccount);
        groupChatRepository.save(groupChat);
        userRepository.save(member);
    }

    public void removeMemberFromChat(GroupChat groupChat, User admin, User member) {
        boolean isMemberAlreadyInChat = isUserExistsInChat(groupChat, member);
        GroupChatAccount adminAccount = accountService.getGroupChatAccount(admin, groupChat);
        GroupChatAccount memberAccount = accountService.getGroupChatAccount(member, groupChat);

        if(!isMemberAlreadyInChat) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not remove user who is not present in the chat");
        }
        if(!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can not remove members from chat because user is not admin");
        }

        int groupMemberIndex = IntStream.range(0, groupChat.getMembers().size())
                .filter(i -> groupChat.getMembers().get(i).getId().equals(memberAccount.getId()))
                .findFirst().orElse(-1);

        int userAccountIndex = IntStream.range(0, member.getGroupChatAccounts().size())
                .filter(i -> member.getGroupChatAccounts().get(i).getId().equals(memberAccount.getId()))
                .findFirst().orElse(-1);

        groupChat.getMembers().remove(groupMemberIndex);
        member.getGroupChatAccounts().remove(userAccountIndex);

        groupChatAccountRepository.delete(memberAccount);
        groupChatRepository.save(groupChat);
        userRepository.save(member);
    }

    public GroupChat createChat(User user, String groupName) {
        GroupChat groupChat = new GroupChat(groupName);
        GroupChatAccount groupChatAccount = new GroupChatAccount(GroupChatRole.ADMIN, groupChat, user);

        groupChat.getMembers().add(groupChatAccount);
        user.getGroupChatAccounts().add(groupChatAccount);

        groupChatRepository.save(groupChat);
        groupChatAccountRepository.save(groupChatAccount);
        userRepository.save(user);

        return groupChat;
    }

    public void setRole(GroupChat groupChat, User admin, User member, GroupChatRole role) {
        boolean isMemberAlreadyInChat = isUserExistsInChat(groupChat, member);
        GroupChatAccount adminAccount = accountService.getGroupChatAccount(admin, groupChat);
        GroupChatAccount memberAccount = accountService.getGroupChatAccount(member, groupChat);

        if(!isMemberAlreadyInChat) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not set role for user who is not present in the chat");
        }
        if(!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can not set role for members from the chat because user is not admin");
        }

        memberAccount.setGroupChatRole(role);

        groupChatRepository.save(groupChat);
        groupChatAccountRepository.save(memberAccount);
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

    public List<User> getOtherUsersOfChat(User currentUser, GroupChat groupChat) {
        List<GroupChatAccount> userAccounts = groupChat.getMembers();
        List<User> users = new ArrayList<>();
        for(GroupChatAccount account : userAccounts) {
            users.add(account.getUser());
        }
        users.stream().filter((User user)-> !user.getId().equals(currentUser.getId()));

        return users;
    }

    public void ensureUserPermissionToSendMessageInChat(User user, UUID chatId) {
        if(!isUserExistsInChat(getChat(chatId), user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User with id " + user.getId() + " can't send messages to other people's chat");
        }
    }

    public GroupMessage saveMessage(SendChatMessageDto sendChatMessageDto, User sender) {
        GroupChat groupChat = getChat(UUID.fromString(sendChatMessageDto.chatId()));
        GroupChatAccount senderAccount = accountService.getGroupChatAccount(sender, groupChat);

        GroupMessage groupChatMessage = new GroupMessage(
                senderAccount,
                groupChat,
                sendChatMessageDto.content(),
                new Date(Long.parseLong(sendChatMessageDto.createdAt()))
        );

        groupChat.getMessages().add(groupChatMessage);
        senderAccount.getGroupMessages().add(groupChatMessage);
        groupChatMessage.setGroupChat(groupChat);
        groupChatMessage.setSender(senderAccount);

        groupChatRepository.save(groupChat);
        groupMessageRepository.save(groupChatMessage);

        return groupChatMessage;
    }
}
