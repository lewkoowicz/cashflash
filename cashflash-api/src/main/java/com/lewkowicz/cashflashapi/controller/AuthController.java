package com.lewkowicz.cashflashapi.controller;

import com.lewkowicz.cashflashapi.constants.AuthConstants;
import com.lewkowicz.cashflashapi.dto.LoginCredentialsDto;
import com.lewkowicz.cashflashapi.dto.PasswordChangeDto;
import com.lewkowicz.cashflashapi.dto.ResponseDto;
import com.lewkowicz.cashflashapi.dto.UserDto;
import com.lewkowicz.cashflashapi.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

    private final AuthServiceImpl authService;
    private final MessageSource messageSource;

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(CsrfToken token) {
        return token;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> signup(@Valid @RequestBody UserDto userDto) {
        authService.signup(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.CREATED.toString(), getMessage(AuthConstants.ACCOUNT_CREATED)));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginCredentialsDto loginRequest) {
        String response = authService.signin(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<ResponseDto> signout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
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

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

}
