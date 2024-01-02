package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.GroupChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage, UUID> {
}
