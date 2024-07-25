package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByLoginOrEmail(String login, String email);
    User findUserByLogin(String login);
}
