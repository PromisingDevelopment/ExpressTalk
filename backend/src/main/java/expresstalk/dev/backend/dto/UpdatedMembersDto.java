package expresstalk.dev.backend.dto;

import expresstalk.dev.backend.entity.User;

import java.util.List;
import java.util.UUID;

public record UpdatedMembersDto (
        UUID chatId,
        List<User> users
) {}
