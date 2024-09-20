package com.lewkowicz.cashflashapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordsDoNotMatchException extends RuntimeException {

    public PasswordsDoNotMatchException(String message) { super(message); }

}
