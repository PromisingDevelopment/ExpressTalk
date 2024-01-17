package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.PrivateChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrivateChatMessageRepository extends JpaRepository<PrivateChatMessage, UUID> {
}
