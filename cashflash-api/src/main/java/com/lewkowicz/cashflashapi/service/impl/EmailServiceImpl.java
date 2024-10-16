package com.lewkowicz.cashflashapi.service.impl;

import com.lewkowicz.cashflashapi.exception.BadRequestException;
import com.lewkowicz.cashflashapi.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void sendConfirmationEmail(String email, String confirmationToken) {
        logger.info("Attempting to send confirmation email to: {}", email);
        if (email == null || email.isEmpty()) {
            logger.info("Confirmation email sending failed. Email address is null or empty");
            throw new IllegalArgumentException("Email address cannot be null or empty");
        }

        Locale locale = LocaleContextHolder.getLocale();
        Context context = new Context(locale);
        context.setVariable("expirationTime", LocalDateTime.now().plusHours(24));

        String confirmationUrl = frontendUrl + "/confirm-email?confirmationToken=" + confirmationToken;
        context.setVariable("confirmationUrl", confirmationUrl);

        sendEmail(email, "email_confirmation", context);
        logger.info("Confirmation email sent successfully to: {}", email);
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetToken) {
        logger.info("Attempting to send password reset email to: {}", email);
        if (email == null || email.isEmpty()) {
            logger.info("Password reset email sending failed. Email address is null or empty");
            throw new IllegalArgumentException("Email address cannot be null or empty");
        }

        Locale locale = LocaleContextHolder.getLocale();
        Context context = new Context(locale);
        context.setVariable("expirationTime", LocalDateTime.now().plusHours(1));

        String resetUrl = frontendUrl + "/password-reset?resetToken=" + resetToken;
        context.setVariable("resetUrl", resetUrl);

        sendEmail(email, "password_reset", context);
        logger.info("Password reset email sent successfully to: {}", email);
    }

    private void sendEmail(String to, String templateName, Context context) {
        try {
            String htmlContent = templateEngine.process(templateName + "_" + context.getLocale().getLanguage(), context);
            String title = extractTitle(htmlContent);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BadRequestException("Failed to send email");
        }
    }

    private String extractTitle(String htmlContent) {
        Pattern pattern = Pattern.compile("<title>(.*?)</title>");
        Matcher matcher = pattern.matcher(htmlContent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "No Title";
    }

}
