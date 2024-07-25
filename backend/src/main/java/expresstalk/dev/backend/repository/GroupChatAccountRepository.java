package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.GroupChatAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupChatAccountRepository extends JpaRepository<GroupChatAccount, UUID> {
}
