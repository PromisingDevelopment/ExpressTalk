package expresstalk.dev.backend.service;

import expresstalk.dev.backend.entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountService {
    public PrivateChatAccount getPrivateChatAccount(User user, PrivateChat privateChat) {
        for (PrivateChatAccount member : privateChat.getMembers()) {
            if(member.getUser().getId().equals(user.getId())) {
                return member;
            }
        }

        return null;
    }

    public GroupChatAccount getGroupChatAccount(User user, GroupChat groupChat) {
        for (GroupChatAccount member : groupChat.getMembers()) {
            if(member.getUser().getId().equals(user.getId())) {
                return member;
            }
        }

        return null;
    }
}
