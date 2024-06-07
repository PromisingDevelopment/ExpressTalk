package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "private_chat_messages")
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class PrivateChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private UUID senderId;

    @NonNull
    @Column(nullable = false)
    private UUID receiverId;

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
}
