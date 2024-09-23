package com.lewkowicz.cashflashapi.controller;

import com.lewkowicz.cashflashapi.constants.UserPreferencesConstants;
import com.lewkowicz.cashflashapi.dto.ResponseDto;
import com.lewkowicz.cashflashapi.dto.UserPreferencesDto;
import com.lewkowicz.cashflashapi.service.IUserPreferencesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping(path = "/api", produces = (MediaType.APPLICATION_JSON_VALUE))
@Validated
@AllArgsConstructor
public class UserPreferencesController {

    private final IUserPreferencesService userPreferencesService;
    private final MessageSource messageSource;

    @PostMapping("/set-default-theme")
    public ResponseEntity<ResponseDto> setDefaultTheme(@Valid @RequestParam Long userId, @RequestParam String theme) {
        userPreferencesService.setDefaultTheme(userId, theme);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(UserPreferencesConstants.DEFAULT_THEME_SET)));
    }

    @PostMapping("/set-default-language")
    public ResponseEntity<ResponseDto> setDefaultLanguage(@Valid @RequestParam Long userId, @RequestParam String language) {
        userPreferencesService.setDefaultLanguage(userId, language);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(UserPreferencesConstants.DEFAULT_LANGUAGE_SET)));
    }

    @GetMapping("/get-user-preferences")
    public ResponseEntity<UserPreferencesDto> getUserPreferences(@Valid @RequestParam Long userId) {
        UserPreferencesDto userPreferencesDto = userPreferencesService.getUserPreferences(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userPreferencesDto);
    }


    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

}
