package com.lewkowicz.cashflashapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeDto {

    @NotEmpty(message = "validation.passwordNotEmpty")
    @Size(min = 8, message = "validation.passwordSize")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "validation.passwordPattern")
    private String currentPassword;

    @NotEmpty(message = "validation.passwordNotEmpty")
    @Size(min = 8, message = "validation.passwordSize")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "validation.passwordPattern")
    private String newPassword;

}
