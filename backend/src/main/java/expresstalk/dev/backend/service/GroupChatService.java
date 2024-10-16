package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.SendChatMessageDto;
import expresstalk.dev.backend.dto.request.SendFileDto;
import expresstalk.dev.backend.dto.response.GroupMessageDto;
import expresstalk.dev.backend.dto.response.GroupMessageDetailsDto;
import expresstalk.dev.backend.dto.response.MessageDto;
import expresstalk.dev.backend.entity.*;
import expresstalk.dev.backend.enums.GroupChatRole;
import expresstalk.dev.backend.exception.ChatNotFoundException;
import expresstalk.dev.backend.exception.UserAbsentInChatException;
import expresstalk.dev.backend.exception.UserNotAdminException;
import expresstalk.dev.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final AttachedFileRepository attachedFileRepository;
    private final SystemMessageRepository systemMessageRepository;

    private boolean isThereAdmins(GroupChat groupChat) {
        for (GroupChatAccount account : groupChat.getMembers()) {
            if(account.getGroupChatRole() == GroupChatRole.ADMIN) return true;
        }

        return false;
    }

    public void ensureUserExistsInChat(User user, GroupChat groupChat) {
        verifyAndGetGroupChatAccount(user, groupChat);
    }

    public GroupChat getChat(UUID chatId) {
        GroupChat groupChat = groupChatRepository.findById(chatId).orElse(null);

        if(groupChat == null) throw new ChatNotFoundException(chatId);

        return groupChat;
    }

    public void addMember(User admin, User member, GroupChat groupChat) {
        GroupChatAccount memberAccount = accountService.getGroupChatAccount(member, groupChat);
        if(memberAccount == null) {
            GroupChatAccount adminAccount = verifyAndGetGroupChatAccount(admin, groupChat);
            memberAccount = new GroupChatAccount(groupChat, member);
            if(!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) throw new UserNotAdminException();

            groupChat.getMembers().add(memberAccount);
            member.getGroupChatAccounts().add(memberAccount);

            groupChatAccountRepository.save(memberAccount);
            groupChatRepository.save(groupChat);
            userRepository.save(member);
        }
    }

    public void removeMember(User admin, User member, GroupChat groupChat) {
        GroupChatAccount memberAccount = verifyAndGetGroupChatAccount(member, groupChat);
        GroupChatAccount adminAccount = verifyAndGetGroupChatAccount(admin, groupChat);
        if(!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) throw new UserNotAdminException();

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

    public GroupChat editChat(String groupName, User admin, GroupChat groupChat) {
        GroupChatAccount adminAccount = verifyAndGetGroupChatAccount(admin, groupChat);
        if(!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) throw new UserNotAdminException();

        groupChat.setName(groupName);

        groupChatRepository.save(groupChat);

        return groupChat;
    }

    @Transactional
    public GroupChat removeChat(User admin, GroupChat groupChat) {
        GroupChatAccount adminAccount = verifyAndGetGroupChatAccount(admin, groupChat);
        if (!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) throw new UserNotAdminException();

        List<GroupChatAccount> accounts = new ArrayList<>(groupChat.getMembers());
        for (GroupChatAccount account : accounts) {
            User user = account.getUser();

            int userAccountId = IntStream.range(0, user.getGroupChatAccounts().size())
                    .filter(i -> user.getGroupChatAccounts().get(i).getId().equals(account.getId()))
                    .findFirst().orElse(-1);
            int groupMemberId = IntStream.range(0, groupChat.getMembers().size())
                    .filter(i -> groupChat.getMembers().get(i).getId().equals(account.getId()))
                    .findFirst().orElse(-1);

            user.getGroupChatAccounts().remove(userAccountId);
            groupChat.getMembers().remove(groupMemberId);
            account.setGroupChat(null);

            groupChatAccountRepository.save(account);
            groupChatRepository.save(groupChat);
            userRepository.save(user);

            Set<GroupMessage> groupMessages = account.getGroupMessages();
            for (GroupMessage groupMessage : groupMessages) {
                groupMessage.setSender(null);
                groupMessage.setGroupChat(null);
                groupMessageRepository.save(groupMessage);
            }

            groupMessageRepository.deleteAll(groupMessages);
            groupChatAccountRepository.delete(account);
        }

        groupChat.setMembers(null);

        List<SystemMessage> systemMessages = groupChat.getSystemMessages();
        for (SystemMessage systemMessage : systemMessages) {
            systemMessage.setChat(null);
            systemMessageRepository.save(systemMessage);
            systemMessageRepository.delete(systemMessage);
        }

        groupChat.setSystemMessages(null);
        groupChatRepository.delete(groupChat);

        return groupChat;
    }

    public void setRole(User admin, User member, GroupChatRole role, GroupChat groupChat) {
        GroupChatAccount adminAccount = verifyAndGetGroupChatAccount(admin, groupChat);
        GroupChatAccount memberAccount = verifyAndGetGroupChatAccount(member, groupChat);
        if(!(adminAccount.getGroupChatRole() == GroupChatRole.ADMIN)) throw new UserNotAdminException();

        memberAccount.setGroupChatRole(role);

        groupChatRepository.save(groupChat);
        groupChatAccountRepository.save(memberAccount);
        userRepository.save(member);
    }

    public List<User> getUsersOfGroupChat(GroupChat groupChat) {
        List<User> users = new ArrayList<>();

        for (GroupChatAccount account : groupChat.getMembers()) {
            users.add(account.getUser());
        }

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

        SendFileDto sendFileDto = sendChatMessageDto.sendFileDto();
        if(sendFileDto != null) {
            AttachedFile attachedFile = new AttachedFile(sendFileDto.name(), sendFileDto.type(), sendFileDto.data());
            attachedFile.setMessage(groupMessage);
            groupMessage.setAttachedFile(attachedFile);

            attachedFileRepository.save(attachedFile);
        }

        groupChat.getMessages().add(groupMessage);
        senderAccount.getGroupMessages().add(groupMessage);
        groupMessage.setGroupChat(groupChat);
        groupMessage.setSender(senderAccount);

        groupChatRepository.save(groupChat);
        groupChatAccountRepository.save(senderAccount);
        groupMessageRepository.save(groupMessage);

        return groupMessage;
    }

    public GroupMessageDto getGroupMessageDto(GroupMessage groupMessage) {
        MessageDto messageDto = new MessageDto(
                groupMessage.getId(),
                groupMessage.getCreatedAt(),
                groupMessage.getContent()
        );

        GroupMessageDetailsDto groupMessageDetailsDto = new GroupMessageDetailsDto(
                groupMessage.getAttachedFile(),
                groupMessage.getSender().getUser().getLogin(),
                groupMessage.getSender().getUser().getId(),
                groupMessage.getSender().getGroupChatRole()
        );

        return new GroupMessageDto(
                messageDto,
                groupMessageDetailsDto,
                false
        );
    }

    public GroupMessageDto getGroupMessageDto(SystemMessage groupMessage) {
        MessageDto messageDto = new MessageDto(
                groupMessage.getId(),
                groupMessage.getCreatedAt(),
                groupMessage.getContent()
        );

        return new GroupMessageDto(
                messageDto,
                null,
                true
        );
    }

    public TreeSet<GroupMessageDto> getGroupMessageDtos(GroupChat groupChat) {
        TreeSet<GroupMessageDto> groupMessageDtos = new TreeSet<>();

        for (GroupMessage message : groupChat.getMessages()) {
            groupMessageDtos.add(getGroupMessageDto(message));
        }

        for (SystemMessage message : groupChat.getSystemMessages()) {
            groupMessageDtos.add(getGroupMessageDto(message));
        }

        return groupMessageDtos;
    }
}
