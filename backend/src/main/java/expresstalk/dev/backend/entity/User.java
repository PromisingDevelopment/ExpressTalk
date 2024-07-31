package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import expresstalk.dev.backend.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "users")
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @NonNull
        @Column(nullable = false)
        private String name;

        @NonNull
        @Column(nullable = false, unique = true)
        private String login;

        @NonNull
        @Column(nullable = false, unique = true)
        @JsonIgnore
        private String email;

        @NonNull
        @Column(nullable = false)
        @JsonIgnore
        private String passwordHash;

        @Column
        @JsonIgnore
        private String emailCode;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, columnDefinition = "varchar(7) default 'ONLINE'")
        private UserStatus status;

        @OneToOne
        @JoinColumn(referencedColumnName = "avatarImageId", name = "id")
        private AvatarImage avatarImage;

        @ToString.Exclude
        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
        @JsonIgnore
        private List<PrivateChatAccount> privateChatAccounts = new LinkedList<>();

        @ToString.Exclude
        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
        @JsonIgnore
        private List<GroupChatAccount> groupChatAccounts = new LinkedList<>();
}
