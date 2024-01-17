package expresstalk.dev.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "private_chats")
@NoArgsConstructor
@Data
public class PrivateChat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "privateChat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PrivateChatMessage> messages = new LinkedList<>();

    @ManyToMany(mappedBy = "privateChats", fetch = FetchType.EAGER)
    private List<User> members = new ArrayList<>();
}
