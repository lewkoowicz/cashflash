package com.lewkowicz.cashflashapi.service;

import com.lewkowicz.cashflashapi.dto.LoginCredentialsDto;
import com.lewkowicz.cashflashapi.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {

    void signup(UserDto userDto);

    void signin(LoginCredentialsDto loginRequest, HttpServletResponse response);

    void changePassword(String email, String currentPassword, String newPassword);

    boolean checkAuth(HttpServletRequest request);

}
