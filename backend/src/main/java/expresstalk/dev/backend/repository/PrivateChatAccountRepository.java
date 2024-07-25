package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.PrivateChatAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrivateChatAccountRepository extends JpaRepository<PrivateChatAccount, UUID> {
}
