package expresstalk.dev.backend.dto.response;

public record PrivateMessageDto(
        MessageDto messageDto,
        PrivateMessageDetailsDto privateMessageDetailsDto,
        boolean isSystemMessage
) implements Comparable<PrivateMessageDto> {
    @Override
    public int compareTo(PrivateMessageDto other) {
        return this.messageDto.createdAt().compareTo(other.messageDto.createdAt());
    }
}
