package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "files")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public abstract class File {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @NonNull
    @Column(nullable = false)
    protected String name;

    @NonNull
    @Column(nullable = false)
    protected String type;

    @NonNull
    @Lob
    @Column(nullable = false)
    protected byte[] data;
}
