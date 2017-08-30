package com.debitum.assets.domain.model.investment;


import java.util.UUID;

public interface InvestmentNotificationService {

    InvestmentNotificationDetails notifyAboutInvestmentAddToBlockchainContract(Investment investment);

    InvestmentNotificationDetails notifyAboutInvestmentPaymentInBlockchainContract(Investment investment);

    InvestmentNotificationDetails notifyAboutInvestmentDelete(Investment investment);

    InvestmentNotificationDetails notifyAboutInvestmentRepayInBlockchainContract(UUID investmentId, UUID invoiceId);

    class InvestmentNotificationDetails {
        private String receiver;
        private String subject;
        private String message;

        public InvestmentNotificationDetails(String receiver,
                                             String subject,
                                             String message) {
            this.receiver = receiver;
            this.subject = subject;
            this.message = message;
        }

        public String getReceiver() {
            return receiver;
        }

        public String getSubject() {
            return subject;
        }

        public String getMessage() {
            return message;
        }

        public final static InvestmentNotificationDetails failedMessage(String message) {
            return new InvestmentNotificationDetails(null, "Notification send failed", message);
        }
    }

}
