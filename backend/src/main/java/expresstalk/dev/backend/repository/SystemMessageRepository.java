package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.SystemMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SystemMessageRepository extends JpaRepository<SystemMessage, UUID> {
}
