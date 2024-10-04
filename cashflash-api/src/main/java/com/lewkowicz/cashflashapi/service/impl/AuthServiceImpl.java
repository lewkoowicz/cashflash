package com.lewkowicz.cashflashapi.service.impl;

import com.lewkowicz.cashflashapi.constants.AuthConstants;
import com.lewkowicz.cashflashapi.dto.LoginCredentialsDto;
import com.lewkowicz.cashflashapi.dto.UserDto;
import com.lewkowicz.cashflashapi.entity.User;
import com.lewkowicz.cashflashapi.exception.AccountAlreadyExistsException;
import com.lewkowicz.cashflashapi.exception.InvalidCredentialsException;
import com.lewkowicz.cashflashapi.exception.LoginFailedException;
import com.lewkowicz.cashflashapi.exception.ResourceNotFoundException;
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

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new AccountAlreadyExistsException(AuthConstants.ACCOUNT_ALREADY_EXISTS);
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
            throw new LoginFailedException(AuthConstants.LOGIN_FAILED);
        }
    }

    @Override
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(
                AuthConstants.USER_NOT_FOUND));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCredentialsException(AuthConstants.INCORRECT_CURRENT_PASSWORD);
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidCredentialsException(AuthConstants.PASSWORDS_CANNOT_BE_THE_SAME);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(LoginCredentialsDto loginCredentialsDto) {
        User user = userRepository.findByEmail(loginCredentialsDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException(
                AuthConstants.USER_NOT_FOUND));
        if (!passwordEncoder.matches(loginCredentialsDto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(AuthConstants.INCORRECT_PASSWORD);
        }
        if (user.getPreferences() != null) {
            userPreferencesRepository.delete(user.getPreferences());
        }
        userRepository.delete(user);
    }

}
