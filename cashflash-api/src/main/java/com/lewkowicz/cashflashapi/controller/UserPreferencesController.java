package com.lewkowicz.cashflashapi.controller;

import com.lewkowicz.cashflashapi.constants.UserPreferencesConstants;
import com.lewkowicz.cashflashapi.dto.ResponseDto;
import com.lewkowicz.cashflashapi.service.IUserPreferencesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

}
