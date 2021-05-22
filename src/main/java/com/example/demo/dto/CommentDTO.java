package com.example.demo.dto;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public class CommentDTO {
    private UUID id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String message;

    public CommentDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
