package com.lewkowicz.cashflashapi.service;

import com.lewkowicz.cashflashapi.dto.LoginCredentialsDto;
import com.lewkowicz.cashflashapi.dto.PasswordForgotDto;
import com.lewkowicz.cashflashapi.dto.PasswordResetDto;
import com.lewkowicz.cashflashapi.dto.UserDto;

public interface IAuthService {

    void signup(UserDto userDto);

    void confirmEmail(String token);

    String signin(LoginCredentialsDto loginRequest);

    void changePassword(String email, String currentPassword, String newPassword);

    void deleteAccount(String email, String delete);

    void initiatePasswordReset(PasswordForgotDto passwordForgotDto);

    void resetPassword(PasswordResetDto passwordResetDto);

}
