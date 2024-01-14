package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupChatRepository extends JpaRepository<GroupChat, UUID> {
}
