package com.lewkowicz.cashflashapi.service;

import com.lewkowicz.cashflashapi.dto.UserPreferencesDto;

public interface IUserPreferencesService {

    void setDefaultTheme(Long userId, String theme);

    void setDefaultLanguage(Long userId, String language);

    UserPreferencesDto getUserPreferences(Long userId);

}
