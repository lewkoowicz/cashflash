package com.lewkowicz.cashflashapi.controller;

import com.lewkowicz.cashflashapi.constants.AuthConstants;
import com.lewkowicz.cashflashapi.dto.*;
import com.lewkowicz.cashflashapi.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping(path = "/api", produces = (MediaType.APPLICATION_JSON_VALUE))
@Validated
@AllArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthServiceImpl authService;
    private final MessageSource messageSource;

    @GetMapping("/csrf-token")
    public String getCsrfToken(CsrfToken token) {
        return token.getToken();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> signup(@Valid @RequestBody UserDto userDto) {
        authService.signup(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.CREATED.toString(), getMessage(AuthConstants.CONFIRMATION_EMAIL_SENT)));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<ResponseDto> confirmEmail(@RequestParam String token) {
        authService.confirmEmail(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(AuthConstants.ACCOUNT_CREATED)));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginCredentialsDto loginRequest) {
        String response = authService.signin(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<ResponseDto> signout(HttpServletRequest request) {
        logger.info("Attempting to sign out user.");
        SecurityContextHolder.clearContext();
        logger.info("Security context cleared.");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            logger.info("User session invalidated.");
        }
        logger.info("User signed out successfully.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(AuthConstants.LOGOUT_SUCCESS)));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseDto> changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto, Authentication authentication) {
        String email = authentication.getName();
        authService.changePassword(email, passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(AuthConstants.PASSWORD_CHANGED)));
    }

    @PostMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@Valid @RequestParam String email, @RequestParam String delete) {
        authService.deleteAccount(email, delete);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDto> forgotPassword(@Valid @RequestBody PasswordForgotDto passwordForgotDto) {
        authService.initiatePasswordReset(passwordForgotDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(AuthConstants.PASSWORD_RESET_REQUEST_RECEIVED)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDto> resetPassword(@Valid @RequestBody PasswordResetDto passwordResetDto) {
        authService.resetPassword(passwordResetDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(AuthConstants.PASSWORD_SUCCESSFULLY_RESET)));
    }

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

}
