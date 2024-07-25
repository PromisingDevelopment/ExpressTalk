package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, UUID> {
}
