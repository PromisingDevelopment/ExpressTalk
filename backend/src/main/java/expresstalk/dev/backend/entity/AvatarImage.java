package expresstalk.dev.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "avatar_images")
@NoArgsConstructor
@Data
public class AvatarImage extends Image {
    @OneToOne
    @JoinColumn(referencedColumnName = "userId", name = "id")
    private User user;

    public AvatarImage(String name, String type, byte[] imageData) {
        super(name, type, imageData);
    }
}
