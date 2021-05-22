package com.example.demo.dto;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

public class UserDTO {

    private UUID id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String surname;

    private String biography;

    public UserDTO() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", surname='" + surname + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}
