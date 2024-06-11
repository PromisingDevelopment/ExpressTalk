package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "private_chat_messages")
@NoArgsConstructor
@Data
public class PrivateChatMessage {
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

    @Column
    private UUID receiverId;

    @Column
    private String receiverLogin;

    @NonNull
    @Column(nullable = false)
    private String content;

    @NonNull
    @Column(nullable = false)
    private Date createdAt;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "private_chats_id", referencedColumnName = "id")
    @JsonIgnore
    private PrivateChat privateChat;

    public PrivateChatMessage(String content, Date createdAt) {
        this.isSystemMessage = true;
        this.content = content;
        this.createdAt = createdAt;
    }

    public PrivateChatMessage(User sender, User receiver, String content, Date createdAt) {
        this.isSystemMessage = false;
        this.senderId  = sender.getId();
        this.senderLogin  = sender.getLogin();
        this.receiverId = receiver.getId();
        this.receiverLogin = receiver.getLogin();
        this.content = content;
        this.createdAt = createdAt;
    }
}
