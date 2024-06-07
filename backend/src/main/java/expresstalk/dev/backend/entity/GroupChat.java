package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "group_chats")
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class GroupChat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Column
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "groupChat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GroupChatMessage> messages = new LinkedList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "groupChats", fetch = FetchType.EAGER)
    private List<User> members = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "administratedGroupChats", fetch = FetchType.EAGER)
    private List<User> admins = new ArrayList<>();
}
