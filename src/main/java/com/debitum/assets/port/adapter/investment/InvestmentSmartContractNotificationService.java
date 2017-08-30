package com.debitum.assets.port.adapter.investment;

import com.debitum.assets.application.investment.InvestmentApplication;
import com.debitum.assets.application.user.UserApplicationService;
import com.debitum.assets.domain.model.investment.CoinPrice;
import com.debitum.assets.domain.model.investment.Investment;
import com.debitum.assets.domain.model.investment.InvestmentNotificationService;
import com.debitum.assets.domain.model.user.User;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

@Component
class InvestmentSmartContractNotificationService implements InvestmentNotificationService {

    private final JavaMailSender javaMailSender;
    private final InvestmentApplication investmentApplication;
    private final UserApplicationService userApplicationService;
    private final SpringTemplateEngine templateEngine;
    private String mailFrom;
    private String debitumUrl;
    private final String investmentContractAddress;
    private final String investmentContractAbi;
    private final String INVESTMENT_ADD_TO_BLOCKCHAIN_EMAIL = "investmentAddToBlockchainEmail";
    private final String INVESTMENT_PAYMENT_IN_BLOCKCHAIN_CONTRACT_EMAIL = "investmentPaymentInBlockchainContractEmail";
    private final String INVESTMENT_INVOICE_REPAID_EMAIL = "investmentInvoiceRepaidEmail";
    private final String INVESTMENT_DELETED_EMAIL = "investmentDeletedEmail";

    Logger log = LoggerFactory.logger(this.getClass());


    public InvestmentSmartContractNotificationService(JavaMailSender javaMailSender,
                                                      InvestmentApplication investmentApplication,
                                                      UserApplicationService userApplicationService,
                                                      SpringTemplateEngine templateEngine,
                                                      @Value("${debitum.contract.investments.address}") String investmentContractAddress,
                                                      @Value("${spring.mail.from}") String mailFrom,
                                                      @Value("${debitum.url}") String debitumUrl,
                                                      @Value("${debitum.contract.investments.abi}") String investmentContractAbi) {
        this.javaMailSender = javaMailSender;
        this.investmentApplication = investmentApplication;
        this.userApplicationService = userApplicationService;
        this.templateEngine = templateEngine;
        this.investmentContractAddress = investmentContractAddress;
        this.mailFrom = mailFrom;
        this.debitumUrl = debitumUrl;
        this.investmentContractAbi = investmentContractAbi;
    }

    @Override
    public InvestmentNotificationDetails notifyAboutInvestmentAddToBlockchainContract(Investment investment) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        User user = userApplicationService.getUser(investment.getUserId());
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("investmentToken",
                investment.getContractToken());

        parameters.put("smartContractAddress",
                investmentContractAddress);

        parameters.put("etherAmount",
                CoinPrice.convertEthAmountToRepresentative(investment.getTotalAmountEth()));


        try {
            return sendNotification(
                    user.getLogin(),
                    INVESTMENT_ADD_TO_BLOCKCHAIN_EMAIL,
                    "DEBITUM investment added to Ethereum blockchain",
                    parameters,
                    mimeMessage);
        } catch (MessagingException e) {
            log.warn(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    user.getLogin(),
                    e.getMessage()));
            return InvestmentNotificationDetails.failedMessage(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    user.getLogin(),
                    e.getMessage()));
        }
    }

    @Override
    public InvestmentNotificationDetails notifyAboutInvestmentPaymentInBlockchainContract(Investment investment) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        User user = userApplicationService.getUser(investment.getUserId());
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("investmentToken",
                investment.getContractToken());


        try {
            return sendNotification(
                    user.getLogin(),
                    INVESTMENT_PAYMENT_IN_BLOCKCHAIN_CONTRACT_EMAIL,
                    "DEBITUM notification about retrieved investment payment in Ethereum blockchain",
                    parameters,
                    mimeMessage);
        } catch (MessagingException e) {
            log.warn(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    user.getLogin(),
                    e.getMessage()));
            return InvestmentNotificationDetails.failedMessage(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    user.getLogin(),
                    e.getMessage()));
        }
    }

    @Override
    public InvestmentNotificationDetails notifyAboutInvestmentDelete(Investment investment) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        User user = userApplicationService.getUser(investment.getUserId());
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("investmentToken",
                investment.getContractToken());


        try {
            return sendNotification(
                    user.getLogin(),
                    INVESTMENT_DELETED_EMAIL,
                    "Debitum investment was deleted",
                    parameters,
                    mimeMessage);
        } catch (MessagingException e) {
            log.warn(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    user.getLogin(),
                    e.getMessage()));
            return InvestmentNotificationDetails.failedMessage(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    user.getLogin(),
                    e.getMessage()));
        }
    }

    @Override
    public InvestmentNotificationDetails notifyAboutInvestmentRepayInBlockchainContract(UUID investmentId, UUID invoiceId) {
        Investment investment = investmentApplication.get(investmentId);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        User user = userApplicationService.getUser(investment.getUserId());
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("investmentToken",
                investment.getContractToken());

        parameters.put("invoiceId",
                invoiceId);


        try {
            return sendNotification(
                    user.getLogin(),
                    INVESTMENT_INVOICE_REPAID_EMAIL,
                    "DEBITUM notification about invoice repayment",
                    parameters,
                    mimeMessage);
        } catch (MessagingException e) {
            log.warn(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    user.getLogin(),
                    e.getMessage()));
            return InvestmentNotificationDetails.failedMessage(String.format("E-mail could not be sent to user '%s', exception is: %s",
                    user.getLogin(),
                    e.getMessage()));
        }
    }

    private InvestmentNotificationDetails sendNotification(String receiver,
                                                           String template,
                                                           String subject,
                                                           HashMap<String, Object> parameters,
                                                           MimeMessage mimeMessage) throws MessagingException {
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                true,
                CharEncoding.UTF_8);
        message.setTo(receiver);
        message.setFrom(mailFrom);
        message.setSubject(subject);


        String content = templateEngine.process(template,
                new Context(Locale.forLanguageTag("en"),
                        parameters));
        message.setText(content,
                true);

        javaMailSender.send(mimeMessage);
        log.info(String.format("Sent e-mail to User %s!",
                receiver));

        return new InvestmentNotificationDetails(receiver, subject, content);
    }
}
