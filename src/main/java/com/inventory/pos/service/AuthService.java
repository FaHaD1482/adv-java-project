package com.inventory.pos.service;

import com.inventory.pos.config.JwtUtil;
import com.inventory.pos.dto.LoginRequestDto;
import com.inventory.pos.dto.RegisterRequestDto;
import com.inventory.pos.entity.User;
import com.inventory.pos.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ✅ REGISTER
    public void register(RegisterRequestDto dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        // 🔥 MUST encode password
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
    }

    // ✅ LOGIN (THIS IS WHERE YOUR ERROR IS)
    public String login(LoginRequestDto dto) {

        // 1️⃣ Find user
        User user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        // 2️⃣ Match password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 3️⃣ Generate JWT
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
