package com.lewkowicz.cashflashapi.service.impl;

import com.lewkowicz.cashflashapi.constants.AuthConstants;
import com.lewkowicz.cashflashapi.dto.LoginCredentialsDto;
import com.lewkowicz.cashflashapi.dto.PasswordForgotDto;
import com.lewkowicz.cashflashapi.dto.PasswordResetDto;
import com.lewkowicz.cashflashapi.dto.UserDto;
import com.lewkowicz.cashflashapi.entity.PasswordReset;
import com.lewkowicz.cashflashapi.entity.User;
import com.lewkowicz.cashflashapi.exception.BadRequestException;
import com.lewkowicz.cashflashapi.repository.PasswordResetRepository;
import com.lewkowicz.cashflashapi.repository.UserPreferencesRepository;
import com.lewkowicz.cashflashapi.repository.UserRepository;
import com.lewkowicz.cashflashapi.security.TokenService;
import com.lewkowicz.cashflashapi.service.IAuthService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenService tokenService;
    private final EmailServiceImpl emailService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void signup(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new BadRequestException(AuthConstants.ACCOUNT_ALREADY_EXISTS);
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole("USER");
        userDetailsService.saveUser(user);
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
