package com.lewkowicz.cashflashapi.service;

public interface IEmailService {

    void sendConfirmationEmail(String email, String confirmationToken);

    void sendPasswordResetEmail(String email, String resetToken);

}
