package com.debitum.assets.domain.model.user;

/**
 * Detailed information about users notification
 */
public class UserNotificationDetails {
    private String receiver;
    private String subject;
    private String message;

    public UserNotificationDetails(String receiver,
                                   String subject,
                                   String message) {
        this.receiver = receiver;
        this.subject = subject;
        this.message = message;
    }

    /**
     * @return notification receiver
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * @return notification subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return
     */
    public String getMessage() {
        return message;
    }

    public final static UserNotificationDetails failedMessage(String message) {
        return new UserNotificationDetails(null, "Notification send failed", message);
    }
}
