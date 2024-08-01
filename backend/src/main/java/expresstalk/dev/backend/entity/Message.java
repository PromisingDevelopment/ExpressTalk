package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "messages")
@Data
@NoArgsConstructor
public abstract class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    protected String content;

    @Column(nullable = false)
    protected Date createdAt;

    @OneToOne
    @JoinColumn(name = "attachedFileId", referencedColumnName = "id")
    private AttachedFile attachedFile;

    public Message(String content, Date createdAt) {
        this.content = content;
        this.createdAt = createdAt;
    }
}
