package expresstalk.dev.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "avatar_files")
@NoArgsConstructor
@Data
public class AvatarFile extends File {
    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    public AvatarFile(String name, String type, byte[] imageData) {
        super(name, type, imageData);
    }
}
