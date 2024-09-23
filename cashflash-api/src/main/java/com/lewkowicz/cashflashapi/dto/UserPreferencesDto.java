package com.lewkowicz.cashflashapi.dto;

import lombok.Data;

@Data
public class UserPreferencesDto {

    private Long userPreferencesId;

    private Long userId;

    private String defaultLanguage;

    private String defaultTheme;

}
