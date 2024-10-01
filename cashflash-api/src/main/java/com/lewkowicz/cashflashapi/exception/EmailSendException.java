package com.lewkowicz.cashflashapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailSendException extends RuntimeException {

    public EmailSendException(String message) {
        super(message);
    }

}
