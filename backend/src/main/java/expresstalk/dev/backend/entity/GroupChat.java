package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "group_chats")
@NoArgsConstructor
@Getter
@Setter
public class GroupChat extends Chat {
    @Column(nullable = false)
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "groupChat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<GroupMessage> messages = new TreeSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "groupChat", fetch = FetchType.EAGER)
    private List<GroupChatAccount> members = new ArrayList<>();

    public GroupChat(String name) {
        this.name = name;
    }
}
