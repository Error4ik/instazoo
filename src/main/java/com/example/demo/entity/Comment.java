package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Comment {

    @Id
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private UUID userId;

    @Column(columnDefinition = "text", nullable = false)
    private String message;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Comment() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id) &&
                post.equals(comment.post) &&
                userId.equals(comment.userId) &&
                createdDate.equals(comment.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, post, userId, createdDate);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", post=" + post +
                ", username='" + username + '\'' +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
