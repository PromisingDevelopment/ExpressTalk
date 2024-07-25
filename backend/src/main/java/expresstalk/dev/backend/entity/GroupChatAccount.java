package expresstalk.dev.backend.entity;

import expresstalk.dev.backend.enums.GroupChatRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "group_chat_accounts")
@NoArgsConstructor
@Data
public class GroupChatAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private GroupChatRole groupChatRole;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GroupMessage> groupMessages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "group_chat_id", referencedColumnName = "id")
    private GroupChat groupChat;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public GroupChatAccount(GroupChatRole groupChatRole, GroupChat groupChat, User user) {
        this.groupChatRole = groupChatRole;
        this.groupChat = groupChat;
        this.user = user;
    }

    public GroupChatAccount(GroupChat groupChat, User user) {
        this(GroupChatRole.MEMBER, groupChat, user);
    }
}
