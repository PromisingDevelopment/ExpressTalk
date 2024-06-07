package expresstalk.dev.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
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
    @OneToMany(mappedBy = "groupChat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupChatMessage> messages = new LinkedList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "groupChats")
    private List<User> members = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "administratedGroupChats")
    private List<User> admins = new ArrayList<>();
}
