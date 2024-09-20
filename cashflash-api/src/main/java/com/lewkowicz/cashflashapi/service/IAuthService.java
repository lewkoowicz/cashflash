package com.lewkowicz.cashflashapi.service;

import com.lewkowicz.cashflashapi.dto.LoginCredentialsDto;
import com.lewkowicz.cashflashapi.dto.UserDto;

import java.util.Map;

public interface IAuthService {

    void createUser(UserDto userDto);

    Map<String, Object> authenticateUser(LoginCredentialsDto loginRequest);

    void deleteAccount(String email, String password);

}
