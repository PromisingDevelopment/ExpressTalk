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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class GroupChatService {
    private final AccountService accountService;
    private final GroupChatAccountRepository groupChatAccountRepository;
    private final GroupChatRepository groupChatRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final UserRepository userRepository;

    public void ensureUserExistsInChat(User user, GroupChat groupChat) {
        verifyAndGetGroupChatAccount(user, groupChat);
    }

    public GroupChat getChat(UUID chatId) {
        GroupChat groupChat = groupChatRepository.findById(chatId).orElse(null);

        if(groupChat == null) throw new ChatIsNotFoundException(chatId);

        return groupChat;
    }

    public void addMemberToChat(User admin, User member, GroupChat groupChat) {
        GroupChatAccount memberAccount = accountService.getGroupChatAccount(member, groupChat);
        if(memberAccount == null) {
            GroupChatAccount adminAccount = verifyAndGetGroupChatAccount(admin, groupChat);
            memberAccount = new GroupChatAccount(groupChat, member);
            if(!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) throw new UserIsNotAdminException();

            groupChat.getMembers().add(memberAccount);

            groupChatAccountRepository.save(memberAccount);
            groupChatRepository.save(groupChat);
            userRepository.save(member);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The member is already in this chat");
        }
    }

    public void removeMemberFromChat(User admin, User member, GroupChat groupChat) {
        GroupChatAccount memberAccount = verifyAndGetGroupChatAccount(member, groupChat);
        GroupChatAccount adminAccount = verifyAndGetGroupChatAccount(admin, groupChat);
        if(!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) throw new UserIsNotAdminException();

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
        GroupChatAccount account = new GroupChatAccount(GroupChatRole.ADMIN, groupChat, user);

        groupChat.getMembers().add(account);
        user.getGroupChatAccounts().add(account);

        groupChatRepository.save(groupChat);
        groupChatAccountRepository.save(account);
        userRepository.save(user);

        return groupChat;
    }

    public void setRole(User admin, User member, GroupChatRole role, GroupChat groupChat) {
        GroupChatAccount adminAccount = verifyAndGetGroupChatAccount(admin, groupChat);
        GroupChatAccount memberAccount = verifyAndGetGroupChatAccount(member, groupChat);
        if(!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) throw new UserIsNotAdminException();

        memberAccount.setGroupChatRole(role);

        groupChatRepository.save(groupChat);
        groupChatAccountRepository.save(memberAccount);
        userRepository.save(member);
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

    public GroupChatAccount verifyAndGetGroupChatAccount(User user, GroupChat groupChat) {
        GroupChatAccount account = accountService.getGroupChatAccount(user, groupChat);
        if(account == null) throw new UserAbsentInChatException(user.getId());

        return account;
    }

    public GroupMessage saveMessage(User sender, SendChatMessageDto sendChatMessageDto) {
        GroupChat groupChat = getChat(UUID.fromString(sendChatMessageDto.chatId()));
        GroupChatAccount senderAccount = verifyAndGetGroupChatAccount(sender, groupChat);
        GroupMessage groupMessage = new GroupMessage(
                senderAccount,
                groupChat,
                sendChatMessageDto.content(),
                new Date(Long.parseLong(sendChatMessageDto.createdAt()))
        );

        groupChat.getMessages().add(groupMessage);
        senderAccount.getGroupMessages().add(groupMessage);
        groupMessage.setGroupChat(groupChat);
        groupMessage.setSender(senderAccount);

        groupChatRepository.save(groupChat);
        groupChatAccountRepository.save(senderAccount);
        groupMessageRepository.save(groupMessage);

        return groupMessage;
    }
}
