package expresstalk.dev.backend.entity;

import jakarta.persistence.*;
import lombok.NonNull;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "private_chat_messages")
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "private_chats_id", referencedColumnName = "id")
    private PrivateChat privateChat;
}
