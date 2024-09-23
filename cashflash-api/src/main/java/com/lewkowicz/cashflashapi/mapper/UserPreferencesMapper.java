package com.lewkowicz.cashflashapi.mapper;

import com.lewkowicz.cashflashapi.dto.UserPreferencesDto;
import com.lewkowicz.cashflashapi.entity.UserPreferences;

public class UserPreferencesMapper {

    public static UserPreferencesDto mapToUserPreferencesDto(UserPreferences userPreferences, UserPreferencesDto userPreferencesDto) {
        userPreferencesDto.setUserPreferencesId(userPreferences.getUserPreferencesId());
        userPreferencesDto.setUserId(userPreferences.getUser().getUserId());
        userPreferencesDto.setDefaultTheme(userPreferences.getDefaultTheme());
        userPreferencesDto.setDefaultLanguage(userPreferences.getDefaultLanguage());
        return userPreferencesDto;
    }

}
