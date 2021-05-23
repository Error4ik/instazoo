package com.example.demo.facade;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setSurname(user.getSurname());
        userDTO.setBiography(user.getBiography());
        return userDTO;
    }
}
