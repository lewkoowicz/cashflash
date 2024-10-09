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
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new BadRequestException(AuthConstants.ACCOUNT_ALREADY_EXISTS);
        }
        Optional<PendingRegistration> existingPendingRegistration = pendingRegistrationRepository.findByEmail(userDto.getEmail());
        if (existingPendingRegistration.isPresent()) {
            PendingRegistration pendingRegistration = existingPendingRegistration.get();
            if (pendingRegistration.getConfirmationTokenExpiryDate().isBefore(LocalDateTime.now())) {
                pendingRegistration.setPassword(passwordEncoder.encode(userDto.getPassword()));
                pendingRegistration.setConfirmationToken(UUID.randomUUID().toString());
                pendingRegistration.setConfirmationTokenExpiryDate(LocalDateTime.now().plusHours(24));
                pendingRegistrationRepository.save(pendingRegistration);
                emailService.sendConfirmationEmail(pendingRegistration.getEmail(), pendingRegistration.getConfirmationToken());
            } else {
                throw new BadRequestException(AuthConstants.CONFIRMATION_EMAIL_ALREADY_SENT);
            }
        } else {
            PendingRegistration newPendingRegistration = new PendingRegistration();
            newPendingRegistration.setEmail(userDto.getEmail());
            newPendingRegistration.setPassword(passwordEncoder.encode(userDto.getPassword()));
            newPendingRegistration.setConfirmationToken(UUID.randomUUID().toString());
            newPendingRegistration.setConfirmationTokenExpiryDate(LocalDateTime.now().plusHours(24));
            pendingRegistrationRepository.save(newPendingRegistration);
            emailService.sendConfirmationEmail(newPendingRegistration.getEmail(), newPendingRegistration.getConfirmationToken());
        }
    }

    @Override
    public void confirmEmail(String token) {
        PendingRegistration pendingRegistration = pendingRegistrationRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid token"));
        if (pendingRegistration.getConfirmationTokenExpiryDate().isBefore(LocalDateTime.now())) {
            pendingRegistrationRepository.delete(pendingRegistration);
            throw new BadRequestException(AuthConstants.CONFIRMATION_TOKEN_EXPIRED);
        }
        User user = new User();
        user.setEmail(pendingRegistration.getEmail());
        user.setPassword(pendingRegistration.getPassword());
        user.setRole("USER");
        userRepository.save(user);
        pendingRegistrationRepository.delete(pendingRegistration);
    }

    @Override
    public String signin(LoginCredentialsDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return tokenService.generateToken(authentication);
        } catch (Exception e) {
            throw new BadRequestException(AuthConstants.LOGIN_FAILED);
        }
    }

    @Override
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(
                AuthConstants.USER_NOT_FOUND));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadRequestException(AuthConstants.INCORRECT_CURRENT_PASSWORD);
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BadRequestException(AuthConstants.PASSWORDS_CANNOT_BE_THE_SAME);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteAccount(String email, String delete) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(
                AuthConstants.USER_NOT_FOUND));
        if (!"DELETE".equals(delete)) {
            throw new BadRequestException(AuthConstants.MESSAGE_DELETE);
        }
        if (user.getPreferences() != null) {
            userPreferencesRepository.delete(user.getPreferences());
        }
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void initiatePasswordReset(PasswordForgotDto passwordForgotDto) {
        User user = userRepository.findByEmail(passwordForgotDto.getEmail()).orElseThrow(() -> new BadRequestException(
                AuthConstants.USER_NOT_FOUND));
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new BadRequestException(AuthConstants.PASSWORD_RESET_DISABLED_FOR_OAUTH);
        }
        if (user.getPasswordReset() != null) {
            passwordResetRepository.delete(user.getPasswordReset());
            passwordResetRepository.flush();
            user.setPasswordReset(null);
        }
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setUser(user);
        String resetToken = UUID.randomUUID().toString();
        passwordReset.setResetPasswordToken(resetToken);
        passwordReset.setResetPasswordTokenExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetRepository.save(passwordReset);
        emailService.sendPasswordResetEmail(passwordForgotDto.getEmail(), resetToken);
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetDto passwordResetDto) {
        PasswordReset passwordReset = passwordResetRepository.findByResetPasswordToken(passwordResetDto.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid token"));
        if (passwordReset.getResetPasswordTokenExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetRepository.delete(passwordReset);
            passwordResetRepository.flush();
            throw new BadRequestException(AuthConstants.RESET_TOKEN_EXPIRED);
        }
        User user = passwordReset.getUser();
        user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
        userRepository.save(user);
        passwordResetRepository.delete(passwordReset);
    }

}
