package com.lewkowicz.cashflashapi.service.impl;

import com.lewkowicz.cashflashapi.constants.AuthConstants;
import com.lewkowicz.cashflashapi.dto.UserPreferencesDto;
import com.lewkowicz.cashflashapi.entity.User;
import com.lewkowicz.cashflashapi.entity.UserPreferences;
import com.lewkowicz.cashflashapi.exception.BadRequestException;
import com.lewkowicz.cashflashapi.mapper.UserPreferencesMapper;
import com.lewkowicz.cashflashapi.repository.UserPreferencesRepository;
import com.lewkowicz.cashflashapi.repository.UserRepository;
import com.lewkowicz.cashflashapi.service.IUserPreferencesService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@AllArgsConstructor
public class UserPreferencesServiceImpl implements IUserPreferencesService {

    private static final Logger logger = LoggerFactory.getLogger(UserPreferencesServiceImpl.class);

    private final UserRepository userRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public void setDefaultTheme(Long userId, String theme) {
        logger.info("Attempting to set default theme: {} for userId: {}", theme, userId);
        UserPreferences userPreferences = getUserPreferencesOrCreate(userId);
        userPreferences.setDefaultTheme(theme);
        userPreferencesRepository.save(userPreferences);
        logger.info("Theme: {} set for userId: {}", theme, userId);
    }

    @Override
    @Transactional
    public void setDefaultLanguage(Long userId, String language) {
        logger.info("Attempting to set default language: {} for userId: {}", language, userId);
        UserPreferences userPreferences = getUserPreferencesOrCreate(userId);
        userPreferences.setDefaultLanguage(language);
        userPreferencesRepository.save(userPreferences);
        logger.info("Language: {} set for userId: {}", language, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserPreferencesDto getUserPreferences(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(getMessage(AuthConstants.USER_NOT_FOUND)));
        if (user.getPreferences() == null) {
            return null;
        }
        return UserPreferencesMapper.mapToUserPreferencesDto(user.getPreferences(), new UserPreferencesDto());
    }

    private UserPreferences getUserPreferencesOrCreate(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(getMessage(AuthConstants.USER_NOT_FOUND)));

        if (user.getPreferences() == null) {
            UserPreferences newPreferences = new UserPreferences();
            newPreferences.setUser(user);
            user.setPreferences(newPreferences);
            userRepository.save(user);
        }

        return user.getPreferences();
    }

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

}
