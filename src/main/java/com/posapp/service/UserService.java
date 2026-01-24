package com.posapp.service;

import com.posapp.dto.UserDTO;
import com.posapp.entity.User;
import com.posapp.exception.ResourceNotFoundException;
import com.posapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO getUserById(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        User user = userOpt.get();
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),
                user.getCreatedAt(), user.getUpdatedAt());
    }

    public UserDTO updateProfile(Integer userId, String email) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        User user = userOpt.get();

        // Check if email is already taken
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            throw new IllegalArgumentException("Email already in use");
        }

        user.setEmail(email);
        userRepository.update(user);

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),
                user.getCreatedAt(), user.getUpdatedAt());
    }
}
