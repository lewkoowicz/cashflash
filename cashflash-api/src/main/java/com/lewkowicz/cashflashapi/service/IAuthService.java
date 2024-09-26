package com.lewkowicz.cashflashapi.service;

import com.lewkowicz.cashflashapi.dto.LoginCredentialsDto;
import com.lewkowicz.cashflashapi.dto.UserDto;

public interface IAuthService {

    void signup(UserDto userDto);

    String signin(LoginCredentialsDto loginRequest);

}
