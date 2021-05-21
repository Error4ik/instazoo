package com.example.demo.payload.request;

import com.example.demo.annotations.PasswordMatches;
import com.example.demo.annotations.ValidEmail;

import javax.validation.constraints.*;

@PasswordMatches
public class SignUpRequest {
    @Email(message = "It must have an email format.")
    @NotBlank(message = "User email is required.")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your name.")
    private String username;

    @NotEmpty(message = "Please enter your surname.")
    private String surname;

    @NotEmpty(message = "Please enter your nickname.")
    private String name;

    @NotEmpty(message = "Password is required.")
    @Size(min = 6)
    private String password;

    @NotEmpty(message = "Please confirm your password.")
    @Size(min = 6)
    private String confirmPassword;

    public SignUpRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
