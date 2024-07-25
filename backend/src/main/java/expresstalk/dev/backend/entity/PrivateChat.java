package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "private_chats")
@NoArgsConstructor
@Data
public class PrivateChat extends Chat {
    @ToString.Exclude
    @OneToMany(mappedBy = "privateChat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PrivateMessage> messages = new LinkedList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "privateChat", fetch = FetchType.EAGER)
    private List<PrivateChatAccount> members = new ArrayList<>();
}
