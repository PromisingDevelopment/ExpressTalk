package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.AvatarImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AvatarImageRepository extends JpaRepository<AvatarImage, UUID> {
}
