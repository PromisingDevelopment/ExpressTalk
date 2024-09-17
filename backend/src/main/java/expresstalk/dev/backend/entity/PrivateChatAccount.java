package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "private_chat_accounts")
@NoArgsConstructor
@Getter
@Setter
public class PrivateChatAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<PrivateMessage> sentMessages = new TreeSet<>();

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "privateChatId", referencedColumnName = "id")
    private PrivateChat privateChat;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
}
