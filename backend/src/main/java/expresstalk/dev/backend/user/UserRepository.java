package expresstalk.dev.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByLoginOrPhone(String login, String phone);
}
