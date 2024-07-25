package expresstalk.dev.backend.entity;

import jakarta.persistence.Column;
import lombok.NonNull;

import java.util.Date;

public abstract class Message {
    @Column(nullable = false)
    protected String content;

    @Column(nullable = false)
    protected Date createdAt;

    public String getContent() { return this.content; }
    public Date getCreatedAt() { return this.createdAt; }
}
