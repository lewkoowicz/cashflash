package com.lewkowicz.cashflashapi.service;

import com.lewkowicz.cashflashapi.dto.LoginCredentialsDto;
import com.lewkowicz.cashflashapi.dto.UserDto;

import java.util.Map;

public interface IAuthService {

    void signup(UserDto userDto);

    Map<String, Object> signin(LoginCredentialsDto loginRequest);

}
