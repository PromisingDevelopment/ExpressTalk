package expresstalk.dev.backend.service;

import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.repository.PrivateChatRepository;
import expresstalk.dev.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class ChatService {
    private final PrivateChatRepository privateChatRepository;
    private final UserRepository userRepository;

    public ChatService(PrivateChatRepository chatRoomRepository, UserRepository userRepository) {
        this.privateChatRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    public PrivateChat createPrivateChatOrGetIfExists(String chatId) {
        PrivateChat chat = privateChatRepository.findPrivateChatByChatId(chatId);

        System.out.println("ChatRoomId: " + chat.getChatId() + "\n");

        if(chat == null) {
            String[] sender_receiver = chatId.split("_");

            UUID senderId = UUID.fromString(sender_receiver[0]);
            UUID receiverId = UUID.fromString(sender_receiver[1]);

            User sender = userRepository.findById(senderId).orElse(null);

            if(sender == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender's id not found.");
            }

            User receiver = userRepository.findById(receiverId).orElse(null);

            if(receiver == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver's id not found.");
            }

            chat = new PrivateChat(
                    chatId,
                    senderId,
                    receiverId
            );

            chat.getMembers().add(sender);
            chat.getMembers().add(receiver);
            sender.getPrivateChats().add(chat);
            receiver.getPrivateChats().add(chat);

            privateChatRepository.save(chat);
            userRepository.save(sender);
            userRepository.save(receiver);
        }

        return chat;
    }
}
