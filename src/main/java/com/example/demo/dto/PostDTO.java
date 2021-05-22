package com.example.demo.dto;

import java.util.Set;
import java.util.UUID;

public class PostDTO {

    private UUID id;

    private String title;

    private String caption;

    private String location;

    private int numberOfLikes;

    private Set<String> usersLiked;

    public PostDTO() {
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

    public Set<String> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(Set<String> usersLiked) {
        this.usersLiked = usersLiked;
    }
}