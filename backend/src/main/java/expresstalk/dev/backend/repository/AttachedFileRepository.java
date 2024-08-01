package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttachedFileRepository extends JpaRepository<AttachedFile, UUID> {
}
