package com.debitum.assets.port.adapter.user.service;


import com.debitum.assets.domain.model.user.UserNotificationDetails;
import com.debitum.assets.domain.model.user.UserNotificationService;
import org.apache.commons.lang.CharEncoding;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;

@Component
class UserAuthorityNotificationService implements UserNotificationService {

    private final JavaMailSender javaMailSender;
    private String mailFrom;
    private String debitumUrl;
    private final SpringTemplateEngine templateEngine;
    private static final String USERS_INITIAL_PASSWORD_CHANGE_EMAIL = "initialPasswordChangeEmail";
    private static final String USERS_PASSWORD_REMINDER_EMAIL = "passwordReminderEmail";

    Logger log = LoggerFactory.logger(this.getClass());

    UserAuthorityNotificationService(
            JavaMailSender javaMailSender,
            SpringTemplateEngine templateEngine,
            @Value("${spring.mail.from}") String mailFrom,
            @Value("${debitum.url}") String debitumUrl) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.mailFrom = mailFrom;
        this.debitumUrl = debitumUrl;
    }

    @Override
    public UserNotificationDetails notifyAboutInitialPasswordSet(String login, String actionTokenKey, Instant tokenExpirationDate) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            return sendNotificationWithActionLink(login,
                    USERS_INITIAL_PASSWORD_CHANGE_EMAIL,
                    "Debitum account created",
                    mimeMessage,
                    actionTokenKey,
                    tokenExpirationDate);
        } catch (Exception e) {
            log.warn(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    login,
                    e.getMessage()));
            return UserNotificationDetails.failedMessage(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    login,
                    e.getMessage()));
        }
    }

    @Override
    public UserNotificationDetails sendPasswordReminder(String login, String actionTokenKey, Instant tokenExpirationDate) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            return sendNotificationWithActionLink(login,
                    USERS_PASSWORD_REMINDER_EMAIL,
                    "Your Debitum password reset request",
                    mimeMessage,
                    actionTokenKey,
                    tokenExpirationDate);
        } catch (Exception e) {
            log.warn(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    login,
                    e.getMessage()));
            return UserNotificationDetails.failedMessage(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    login,
                    e.getMessage()));
        }
    }

    private UserNotificationDetails sendNotificationWithActionLink(String receiver,
                                                                   String template,
                                                                   String subject,
                                                                   MimeMessage mimeMessage,
                                                                   String activationKey,
                                                                   Instant tokenExpirationDate) throws MessagingException {
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                true,
                CharEncoding.UTF_8);
        message.setTo(receiver);
        message.setFrom(mailFrom);
        message.setSubject(subject);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("activationKey",
                activationKey);

        parameters.put("debitumUrl",
                debitumUrl);

        parameters.put("login",
                receiver);

        parameters.put("tokenExpirationDate", tokenExpirationDate);

        String content = templateEngine.process(template,
                new Context(Locale.forLanguageTag("en"),
                        parameters));
        message.setText(content,
                true);

        javaMailSender.send(mimeMessage);
        log.info(String.format("Sent e-mail to User %s!",
                receiver));

        return new UserNotificationDetails(receiver, subject, content);
    }
}
