package com.lewkowicz.cashflashapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    private Long userId;

    @NotEmpty(message = "validation.emailNotEmpty")
    @Email(message = "validation.emailInvalid")
    private String email;

    @NotEmpty(message = "validation.passwordNotEmpty")
    @Size(min = 8, message = "validation.passwordSize")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "validation.passwordPattern")
    private String password;

    private String role;

}
