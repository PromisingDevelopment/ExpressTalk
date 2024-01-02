package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "private_chats")
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class PrivateChat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // It's senderId_receiverId field
    @NonNull
    @Column(nullable = false)
    private String chatId;

    @NonNull
    @Column(nullable = false)
    private UUID senderId;

    @NonNull
    @Column(nullable = false)
    private UUID receiverId;

    @OneToMany(mappedBy = "privateChat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrivateChatMessage> messages = new LinkedList<>();

    @ManyToMany(mappedBy = "privateChats")
    @JsonIgnore
    private List<User> members = new ArrayList<>();
}
