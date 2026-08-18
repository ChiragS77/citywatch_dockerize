package com.userservice.service;

import com.userservice.dto.ForgotPasswordRequest;
import com.userservice.dto.MessageResponse;
import com.userservice.dto.ResetPasswordRequest;
import com.userservice.entity.PasswordResetToken;
import com.userservice.entity.User;
import com.userservice.repo.PasswordResetTokenRepository;
import com.userservice.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public MessageResponse forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(user.getEmail());
        resetToken.setToken(token);
        resetToken.setExpiryTime(LocalDateTime.now().plusMinutes(15));
        resetToken.setUsed(false);

        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:4200/reset-password?token=" + token;

        emailService.sendEmail(
                user.getEmail(),
                "Reset Password - CityWatch",
                "Hello " + user.getName() + ",\n\n"
                        + "Click the link below to reset your CityWatch password:\n\n"
                        + resetLink + "\n\n"
                        + "This link will expire in 15 minutes.\n\n"
                        + "If you did not request this, please ignore this email.\n\n"
                        + "Regards,\nCityWatch Team"
        );

        return new MessageResponse("Reset password link sent to your email.");
    }

    public MessageResponse resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isUsed()) {
            throw new RuntimeException("Token already used");
        }

        if (resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return new MessageResponse("Password reset successfully");
    }
}
