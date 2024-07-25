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
@Table(name = "group_messages")
@NoArgsConstructor
@Data
public class GroupMessage extends Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private GroupChatAccount sender;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "group_chat_id", referencedColumnName = "id")
    @JsonIgnore
    private GroupChat groupChat;

    public GroupMessage(GroupChatAccount sender, GroupChat groupChat, String content, Date createdAt) {
        this.sender = sender;
        this.groupChat = groupChat;
        this.content = content;
        this.createdAt = createdAt;
    }
}
