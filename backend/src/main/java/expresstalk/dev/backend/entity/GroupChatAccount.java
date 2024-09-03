package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import expresstalk.dev.backend.enums.GroupChatRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
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

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private TreeSet<GroupMessage> groupMessages = new TreeSet<>();

    @ManyToOne
    @JoinColumn(name = "groupChatId", referencedColumnName = "id")
    private GroupChat groupChat;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
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
