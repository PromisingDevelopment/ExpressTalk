package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "private_messages")
@NoArgsConstructor
@Getter
@Setter
public class PrivateMessage extends Message {
    @ManyToOne
    @JoinColumn(name = "senderId", referencedColumnName = "id")
    private PrivateChatAccount sender;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "privateChatId", referencedColumnName = "id")
    @JsonIgnore
    private PrivateChat privateChat;

    public PrivateMessage(PrivateChatAccount sender, PrivateChat privateChat, String content, Date createdAt) {
        super(content, createdAt);
        this.sender = sender;
        this.privateChat = privateChat;
    }
}
