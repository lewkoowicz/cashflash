package com.lewkowicz.cashflashapi.service.impl;

import com.lewkowicz.cashflashapi.exception.EmailSendException;
import com.lewkowicz.cashflashapi.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
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

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendLoginNotification(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email address cannot be null or empty");
        }

        Locale locale = LocaleContextHolder.getLocale();
        Context context = new Context(locale);
        context.setVariable("email", email);
        context.setVariable("loginTime", LocalDateTime.now());

        sendEmail(email, "test", context);
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
            throw new EmailSendException("Failed to send email");
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
