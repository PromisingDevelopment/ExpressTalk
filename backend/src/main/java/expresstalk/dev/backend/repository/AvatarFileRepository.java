package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.AvatarFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AvatarFileRepository extends JpaRepository<AvatarFile, UUID> {
}
