package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "system_messages")
@NoArgsConstructor
@Getter
@Setter
public class SystemMessage extends Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private boolean isSystemMessage = true;

    @ManyToOne
    @JoinColumn(name = "chatId", referencedColumnName = "id")
    private Chat chat;

    public SystemMessage(String content, Date createdAt, Chat chat) {
        this.content = content;
        this.createdAt = createdAt;
        this.chat = chat;
    }
}
