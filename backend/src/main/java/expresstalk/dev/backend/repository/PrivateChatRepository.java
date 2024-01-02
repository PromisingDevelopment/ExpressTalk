package expresstalk.dev.backend.repository;

import expresstalk.dev.backend.entity.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrivateChatRepository extends JpaRepository<PrivateChat, UUID> {
    public PrivateChat findPrivateChatByChatId(String chatId);
}
