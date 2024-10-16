package com.lewkowicz.cashflashapi.service.impl;

import com.lewkowicz.cashflashapi.constants.AuthConstants;
import com.lewkowicz.cashflashapi.dto.LoginCredentialsDto;
import com.lewkowicz.cashflashapi.dto.PasswordForgotDto;
import com.lewkowicz.cashflashapi.dto.PasswordResetDto;
import com.lewkowicz.cashflashapi.dto.UserDto;
import com.lewkowicz.cashflashapi.entity.PasswordReset;
import com.lewkowicz.cashflashapi.entity.PendingRegistration;
import com.lewkowicz.cashflashapi.entity.User;
import com.lewkowicz.cashflashapi.exception.BadRequestException;
import com.lewkowicz.cashflashapi.repository.PasswordResetRepository;
import com.lewkowicz.cashflashapi.repository.PendingRegistrationRepository;
import com.lewkowicz.cashflashapi.repository.UserPreferencesRepository;
import com.lewkowicz.cashflashapi.repository.UserRepository;
import com.lewkowicz.cashflashapi.security.TokenService;
import com.lewkowicz.cashflashapi.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final TokenService tokenService;
    private final EmailServiceImpl emailService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void signup(UserDto userDto) {
        logger.info("Attempting to sign up user: {}", userDto.getEmail());
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            logger.info("Signup attempt failed. Account already exists for email: {}", userDto.getEmail());
            throw new BadRequestException(AuthConstants.ACCOUNT_ALREADY_EXISTS);
        }
        Optional<PendingRegistration> existingPendingRegistration = pendingRegistrationRepository.findByEmail(userDto.getEmail());
        if (existingPendingRegistration.isPresent()) {
            PendingRegistration pendingRegistration = existingPendingRegistration.get();
            if (pendingRegistration.getConfirmationTokenExpiryDate().isBefore(LocalDateTime.now())) {
                logger.info("Updating existing expired pending registration token for email: {}", userDto.getEmail());
                pendingRegistration.setPassword(passwordEncoder.encode(userDto.getPassword()));
                pendingRegistration.setConfirmationToken(UUID.randomUUID().toString());
                pendingRegistration.setConfirmationTokenExpiryDate(LocalDateTime.now().plusHours(24));
                pendingRegistrationRepository.save(pendingRegistration);
                emailService.sendConfirmationEmail(pendingRegistration.getEmail(), pendingRegistration.getConfirmationToken());
                logger.info("Expired pending registration token for email: {} updated", userDto.getEmail());
            } else {
                logger.info("Signup attempt failed: Confirmation email already sent for email: {}", userDto.getEmail());
                throw new BadRequestException(AuthConstants.CONFIRMATION_EMAIL_ALREADY_SENT);
            }
        } else {
            logger.info("Creating new pending registration for email: {}", userDto.getEmail());
            PendingRegistration newPendingRegistration = new PendingRegistration();
            newPendingRegistration.setEmail(userDto.getEmail());
            newPendingRegistration.setPassword(passwordEncoder.encode(userDto.getPassword()));
            newPendingRegistration.setConfirmationToken(UUID.randomUUID().toString());
            newPendingRegistration.setConfirmationTokenExpiryDate(LocalDateTime.now().plusHours(24));
            pendingRegistrationRepository.save(newPendingRegistration);
            emailService.sendConfirmationEmail(newPendingRegistration.getEmail(), newPendingRegistration.getConfirmationToken());
            logger.info("Created new pending registration and sent confirmation email for: {}", newPendingRegistration.getEmail());
        }
    }

    @Override
    @Transactional
    public void confirmEmail(String token) {
        logger.info("Attempting to confirm email with token: {}", token);
        PendingRegistration pendingRegistration = pendingRegistrationRepository.findByConfirmationToken(token)
                .orElseThrow(() -> {
                    logger.info("Email confirmation failed. Invalid token.");
                    return new BadRequestException("Invalid token");
                });
        if (pendingRegistration.getConfirmationTokenExpiryDate().isBefore(LocalDateTime.now())) {
            logger.info("Confirmation token expired for email: {}", pendingRegistration.getEmail());
            pendingRegistrationRepository.delete(pendingRegistration);
            logger.info("Expired confirmation token for email: {} deleted", pendingRegistration.getEmail());
            throw new BadRequestException(AuthConstants.CONFIRMATION_TOKEN_EXPIRED);
        }
        User user = new User();
        user.setEmail(pendingRegistration.getEmail());
        user.setPassword(pendingRegistration.getPassword());
        user.setRole("USER");
        userRepository.save(user);
        logger.info("Created new user account for email: {}", user.getEmail());
        pendingRegistrationRepository.delete(pendingRegistration);
        logger.info("Deleted pending registration for email: {}", pendingRegistration.getEmail());
    }

    @Override
    public String signin(LoginCredentialsDto loginRequest) {
        logger.info("Attempting to sign in user: {}", loginRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("User signed in successfully: {}", loginRequest.getEmail());
            return tokenService.generateToken(authentication);
        } catch (Exception e) {
            logger.info("Login failed for user: {}. Reason: {}", loginRequest.getEmail(), e.getMessage());
            throw new BadRequestException(AuthConstants.LOGIN_FAILED);
        }
    }

    @Override
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        logger.info("Attempting to change password for user: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            logger.info("Password change failed. User not found: {}", email);
            return new BadRequestException(AuthConstants.USER_NOT_FOUND);
        });
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            logger.info("Password change failed. Incorrect current password for user: {}", email);
            throw new BadRequestException(AuthConstants.INCORRECT_CURRENT_PASSWORD);
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            logger.info("Password change failed. New password same as current for user: {}", email);
            throw new BadRequestException(AuthConstants.PASSWORDS_CANNOT_BE_THE_SAME);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("Password changed successfully for user: {}", email);
    }

    @Override
    @Transactional
    public void deleteAccount(String email, String delete) {
        logger.info("Attempting to delete account for user: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            logger.info("Account deletion failed. User not found: {}", email);
            return new BadRequestException(AuthConstants.USER_NOT_FOUND);
        });
        if (!"DELETE".equals(delete)) {
            logger.info("Account deletion failed. Invalid delete confirmation for user: {}", email);
            throw new BadRequestException(AuthConstants.MESSAGE_DELETE);
        }
        if (user.getPreferences() != null) {
            userPreferencesRepository.delete(user.getPreferences());
            logger.info("Deleted user preferences for user: {}", email);
        }
        userRepository.delete(user);
        logger.info("Account deleted successfully for user: {}", email);
    }

    @Override
    @Transactional
    public void initiatePasswordReset(PasswordForgotDto passwordForgotDto) {
        logger.info("Initiating password reset for user: {}", passwordForgotDto.getEmail());
        User user = userRepository.findByEmail(passwordForgotDto.getEmail()).orElseThrow(() -> {
            logger.info("Password reset failed. User not found: {}", passwordForgotDto.getEmail());
            return new BadRequestException(AuthConstants.USER_NOT_FOUND);
        });
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            logger.info("Password reset failed. OAuth user cannot reset password: {}", passwordForgotDto.getEmail());
            throw new BadRequestException(AuthConstants.PASSWORD_RESET_DISABLED_FOR_OAUTH);
        }
        if (user.getPasswordReset() != null) {
            passwordResetRepository.delete(user.getPasswordReset());
            passwordResetRepository.flush();
            user.setPasswordReset(null);
            logger.info("Deleted existing password reset for user: {}", passwordForgotDto.getEmail());
        }
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setUser(user);
        String resetToken = UUID.randomUUID().toString();
        passwordReset.setResetPasswordToken(resetToken);
        passwordReset.setResetPasswordTokenExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetRepository.save(passwordReset);
        emailService.sendPasswordResetEmail(passwordForgotDto.getEmail(), resetToken);
        logger.info("Password reset initiated and email sent for user: {}", passwordForgotDto.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetDto passwordResetDto) {
        logger.info("Attempting to reset password with token: {}", passwordResetDto.getToken());
        PasswordReset passwordReset = passwordResetRepository.findByResetPasswordToken(passwordResetDto.getToken())
                .orElseThrow(() -> {
                    logger.info("Password reset failed. Invalid token: {}", passwordResetDto.getToken());
                    return new BadRequestException("Invalid token");
                });
        if (passwordReset.getResetPasswordTokenExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetRepository.delete(passwordReset);
            passwordResetRepository.flush();
            logger.info("Password reset failed. Token expired for user: {}", passwordReset.getUser().getEmail());
            throw new BadRequestException(AuthConstants.RESET_TOKEN_EXPIRED);
        }
        User user = passwordReset.getUser();
        user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
        userRepository.save(user);
        passwordResetRepository.delete(passwordReset);
        logger.info("Password reset successfully for user: {}", user.getEmail());
    }

}
