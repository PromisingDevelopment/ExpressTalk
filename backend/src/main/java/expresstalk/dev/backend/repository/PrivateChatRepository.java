package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PrivateChatRepository extends JpaRepository<PrivateChat, UUID> {
    @Query("SELECT pc FROM PrivateChat pc " +
            "JOIN pc.members a1 " +
            "JOIN pc.members a2 " +
            "WHERE a1.user = :member1 " +
            "AND a2.user = :member2")
    PrivateChat findPrivateChatBetween(@Param("member1") User member1, @Param("member2") User member2);
}
