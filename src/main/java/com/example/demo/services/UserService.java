package com.example.demo.services;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.ERole;
import com.example.demo.exceptions.UserExistException;
import com.example.demo.payload.request.SignUpRequest;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
public class UserService {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignUpRequest userIn) {
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setUsername(userIn.getUsername());
        user.setSurname(userIn.getSurname());
        user.setName(userIn.getName());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoleSet().add(ERole.ROLE_USER);

        try {
            LOGGER.info("Saving user {}", userIn.getEmail());
            return this.userRepository.save(user);
        } catch (Exception ex) {
            LOGGER.error("Error during registration {}", ex.getMessage());
            throw new UserExistException("The user already exist. Please check credentials.");
        }
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = this.getUserByPrincipal(principal);
        user.setUsername(userDTO.getUsername());
        user.setSurname(userDTO.getSurname());
        user.setBiography(userDTO.getBiography());
        return this.userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return this.getUserByPrincipal(principal);
    }

    public User getUserById(UUID userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private User getUserByPrincipal(Principal principal) {
        return this.userRepository.findUserByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username not found %s", principal.getName())));
    }
}
