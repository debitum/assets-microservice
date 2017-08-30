package com.debitum.assets.resource.config;

import org.apache.commons.lang3.Validate;
import org.springframework.core.ErrorCoded;

import java.util.function.Supplier;

public class ErrorCodedException extends RuntimeException implements ErrorCoded {

    private String code;

    public ErrorCodedException(String code) {
        Validate.notNull(code);
        this.code = code;
    }

    public ErrorCodedException(String code, String message) {
        super(message);
        Validate.notNull(code);
        this.code = code;
    }

    public static Supplier<ErrorCodedException> errorCodedSupplier(String errorCode) {
        return () -> new ErrorCodedException(errorCode);
    }

    public static Supplier<ErrorCodedException> errorCodedSupplier(String errorCode, String message) {
        return () -> new ErrorCodedException(errorCode, message);
    }

    @Override
    public String getErrorCode() {
        return code;
    }
}
