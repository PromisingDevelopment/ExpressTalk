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
@Getter
@Setter
public class PrivateChat extends Chat {
    @ToString.Exclude
    @OneToMany(mappedBy = "privateChat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PrivateMessage> messages = new TreeSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "privateChat", fetch = FetchType.EAGER)
    private List<PrivateChatAccount> members = new ArrayList<>();
}
