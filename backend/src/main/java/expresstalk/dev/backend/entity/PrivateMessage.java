package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "private_messages")
@NoArgsConstructor
@Data
public class PrivateMessage extends Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "senderId", referencedColumnName = "id")
    private PrivateChatAccount sender;

    @ManyToOne
    @JoinColumn(name = "receiverId", referencedColumnName = "id")
    private PrivateChatAccount receiver;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "privateChatId", referencedColumnName = "id")
    @JsonIgnore
    private PrivateChat privateChat;

    public PrivateMessage(PrivateChatAccount sender, PrivateChatAccount receiver, PrivateChat privateChat, String content, Date createdAt) {
        this.sender = sender;
        this.receiver = receiver;
        this.privateChat = privateChat;
        this.content = content;
        this.createdAt = createdAt;
    }
}
