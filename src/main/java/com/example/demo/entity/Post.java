package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Post {

    @Id
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;

    private String caption;

    private String location;

    private int numberOfLikes;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> likedUsers;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public Post() {
        this.likedUsers = new HashSet<>();
        this.comments = new ArrayList<>();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<String> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(Set<String> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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
        Post post = (Post) o;
        return id.equals(post.id) &&
                user.equals(post.user) &&
                createdDate.equals(post.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, createdDate);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", caption='" + caption + '\'' +
                ", location='" + location + '\'' +
                ", numberOfLikes=" + numberOfLikes +
                ", user=" + user +
                ", createdDate=" + createdDate +
                '}';
    }
}
