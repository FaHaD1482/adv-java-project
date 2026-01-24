package com.posapp.controller;

import com.posapp.dto.UserDTO;
import com.posapp.service.UserService;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Authentication authentication) {
        Integer userId = (Integer) authentication.getDetails();
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(
            @RequestParam @Email String email,
            Authentication authentication) {
        Integer userId = (Integer) authentication.getDetails();
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        UserDTO userDTO = userService.updateProfile(userId, email);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id, Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .findFirst()
                .orElse("");

        if (!role.contains("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }
}
