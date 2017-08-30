package com.debitum.assets.domain.model;

import org.apache.commons.lang3.Validate;

import java.text.MessageFormat;
import java.util.Collection;

public class Preconditions {

    private static Validate apacheValidate;

    public static void isTrue(boolean expression,
                              String message,
                              Object... values) {
        apacheValidate.isTrue(
                expression,
                getMessage(
                        message,
                        values
                )
        );
    }

    public static void isTrue(boolean expression,
                              DomainException domainException) {
        try {
            apacheValidate.isTrue(expression);
        } catch (IllegalArgumentException ex) {
            throw domainException;
        }
    }

    public static <T> T notNull(T reference,
                                String message,
                                Object... values) {
        return apacheValidate.notNull(
                reference,
                getMessage(
                        message,
                        values
                )
        );
    }

    public static <T> T notNull(T reference,
                                DomainException domainException) {
        try {
            return apacheValidate.notNull(reference);
        } catch (NullPointerException ex) {
            throw domainException;
        }
    }

    public static <T extends Collection<?>> T notEmpty(T collection,
                                                       String message,
                                                       Object... values) {
        return apacheValidate.notEmpty(
                collection,
                getMessage(
                        message,
                        values
                )
        );
    }

    public static <T extends Collection<?>> T notEmpty(T collection,
                                                       DomainException domainException) {
        try {
            return apacheValidate.notEmpty(collection);
        } catch (Exception ex) {
            throw domainException;
        }
    }

    public static <T extends CharSequence> T notBlank(T chars,
                                                      String message,
                                                      Object... values) {
        return apacheValidate.notBlank(
                chars,
                getMessage(
                        message,
                        values
                )
        );
    }

    public static <T extends CharSequence> T notBlank(T chars,
                                                      DomainException domainException) {
        try {
            return apacheValidate.notBlank(chars);
        } catch (Exception ex) {
            throw domainException;
        }
    }

    private static String getMessage(String message,
                                     Object[] values) {
        return MessageFormat.format(
                message,
                values
        );
    }
}
