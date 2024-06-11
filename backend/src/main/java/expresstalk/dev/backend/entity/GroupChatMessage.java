package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "group_chat_messages")
@NoArgsConstructor
@Data
public class GroupChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private boolean isSystemMessage;

    @Column
    private UUID senderId;

    @Column
    private String senderLogin;

    @NonNull
    @Column(nullable = false)
    private String content;

    @NonNull
    @Column(nullable = false)
    private Date createdAt;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_chats_id", referencedColumnName = "id")
    private GroupChat groupChat;

    public GroupChatMessage(String content, Date createdAt) {
        this.isSystemMessage = true;
        this.content = content;
        this.createdAt = createdAt;
    }

    public GroupChatMessage(User sender, String content, Date createdAt) {
        this.isSystemMessage = false;
        this.senderId  = sender.getId();
        this.senderLogin  = sender.getLogin();
        this.content = content;
        this.createdAt = createdAt;
    }
}
