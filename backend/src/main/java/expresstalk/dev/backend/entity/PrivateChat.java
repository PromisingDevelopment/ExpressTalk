package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "private_chats")
@NoArgsConstructor
@Data
public class PrivateChat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Exclude
    @OneToMany(mappedBy = "privateChat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PrivateChatMessage> messages = new LinkedList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "privateChats", fetch = FetchType.EAGER)
    private List<User> members = new ArrayList<>();
}
