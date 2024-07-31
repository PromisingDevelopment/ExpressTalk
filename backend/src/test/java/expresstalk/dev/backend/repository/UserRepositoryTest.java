package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.test_utils.TestValues;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldFindUserByLoginOrEmail() {
        User user = TestValues.getUser();
        userRepository.save(user);

        User result = userRepository.findUserByLoginOrEmail(user.getLogin(), user.getEmail());
        assertEquals(result.getLogin(), user.getLogin());
        result = userRepository.findUserByLoginOrEmail(user.getLogin(), user.getLogin());
        assertEquals(result.getLogin(), user.getLogin());
        result = userRepository.findUserByLoginOrEmail(user.getEmail(), user.getEmail());
        assertEquals(result.getLogin(), user.getLogin());
    }
    @Test
    void shouldFindUserByLogin() {
        User user = TestValues.getUser();
        userRepository.save(user);

        User result = userRepository.findUserByLogin(user.getLogin());
        assertEquals(result.getLogin(), user.getLogin());
    }
}