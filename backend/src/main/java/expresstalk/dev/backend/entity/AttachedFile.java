package expresstalk.dev.backend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "attached_files")
@NoArgsConstructor
@Data
public class AttachedFile extends File {
    @OneToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    @JsonIgnore
    private Message message;

    public AttachedFile(String name, String type, byte[] data) {
        super(name, type, data);
    }
}
