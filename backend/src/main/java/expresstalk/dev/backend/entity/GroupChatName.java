package expresstalk.dev.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "group_chats_names")
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class GroupChatName {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private String name;

    // List with entities of the same group but for different members of the group;
    @OneToMany(mappedBy = "name", cascade = CascadeType.ALL)
    private List<GroupChat> groupSiblings = new LinkedList<>();
}
