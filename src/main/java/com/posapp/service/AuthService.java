package com.posapp.service;

import com.posapp.dto.LoginRequestDTO;
import com.posapp.dto.LoginResponseDTO;
import com.posapp.dto.UserRegistrationDTO;
import com.posapp.dto.UserDTO;
import com.posapp.entity.User;
import com.posapp.exception.BadRequestException;
import com.posapp.exception.UnauthorizedException;
import com.posapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Value("${jwt.expiry}")
    private long jwtExpiry;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public UserDTO register(UserRegistrationDTO registrationDTO) {
        // Check if username exists
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        // Check if email exists
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        // Validate password strength
        validatePassword(registrationDTO.getPassword());

        // Create user
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(registrationDTO.getRole());

        User savedUser = userRepository.save(user);

        return new UserDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }

    public LoginResponseDTO login(LoginRequestDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByUsername(loginDTO.getUsername());

        if (userOpt.isEmpty() || !passwordEncoder.matches(loginDTO.getPassword(), userOpt.get().getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        User user = userOpt.get();
        String token = jwtService.generateToken(user.getUsername(), user.getRole(), user.getId());

        return new LoginResponseDTO(token, user.getUsername(), user.getRole(), jwtExpiry);
    }

    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new UnauthorizedException("User not found");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        validatePassword(newPassword);

        userRepository.updatePassword(userId, passwordEncoder.encode(newPassword));
    }

    public void forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new BadRequestException("Email not found");
        }

        User user = userOpt.get();
        String resetToken = UUID.randomUUID().toString();

        userRepository.updateResetToken(user.getId(), resetToken);
        emailService.sendResetPasswordEmail(user.getEmail(), resetToken);
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> userOpt = userRepository.findByResetToken(token);

        if (userOpt.isEmpty()) {
            throw new BadRequestException("Invalid reset token");
        }

        validatePassword(newPassword);

        User user = userOpt.get();
        userRepository.updatePassword(user.getId(), passwordEncoder.encode(newPassword));
        userRepository.updateResetToken(user.getId(), null);
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new BadRequestException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new BadRequestException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new BadRequestException("Password must contain at least one digit");
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?].*")) {
            throw new BadRequestException("Password must contain at least one special character");
        }
    }
}
