package expresstalk.dev.backend.dto.response;

public record GroupMessageDto(
        MessageDto messageDto,
        GroupMessageDetailsDto groupMessageDetailsDto,
        boolean isSystemMessage
) implements Comparable<GroupMessageDto> {
    @Override
    public int compareTo(GroupMessageDto other) {
       return this.messageDto.createdAt().compareTo(other.messageDto.createdAt());
    }
}

