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
@Table(name = "group_messages")
@NoArgsConstructor
@Getter
@Setter
public class GroupMessage extends Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "senderId", referencedColumnName = "id")
    private GroupChatAccount sender;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "groupChatId", referencedColumnName = "id")
    @JsonIgnore
    private GroupChat groupChat;

    public GroupMessage(GroupChatAccount sender, GroupChat groupChat, String content, Date createdAt) {
        super(content, createdAt);
        this.sender = sender;
        this.groupChat = groupChat;
    }
}
