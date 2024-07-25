package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, UUID> {
}
