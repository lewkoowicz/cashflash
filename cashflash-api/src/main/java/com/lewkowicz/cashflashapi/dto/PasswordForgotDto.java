package com.lewkowicz.cashflashapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordForgotDto {

    @NotEmpty(message = "validation.emailNotEmpty")
    @Email(message = "validation.emailInvalid")
    private String email;

}
