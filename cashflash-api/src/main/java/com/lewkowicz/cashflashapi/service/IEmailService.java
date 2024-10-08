package com.lewkowicz.cashflashapi.service;

public interface IEmailService {

    void sendPasswordResetEmail(String email, String resetToken);

}
