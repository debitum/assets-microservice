package com.debitum.assets.resource.config;


import com.debitum.assets.domain.model.DomainException;
import com.debitum.assets.resource.helpers.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception handling flow:
 * <p>
 * All expected concrete exceptions should be returned as 400 with ErrorDTO
 * All not concrete unexpected exceptions should be 500 without any ErrorDTO
 */
@ControllerAdvice
class GlobalControllerExceptionHandler {

    public static final String UNEXPECTED_ERROR_CODE = "UNEXPECTED_ERROR";
    public static final String ACCESS_DENIED_ERROR_CODE = "UNEXPECTED_ERROR";

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handle(Exception e) {
        //        TODO: don't return all errors to client

        logException(e);
        return new ErrorDTO(
                UNEXPECTED_ERROR_CODE,
                e.getMessage(),
                org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e)
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO handle(AccessDeniedException e) {
        //        TODO: don't return all errors to client

        logException(e);
        return new ErrorDTO(
                ACCESS_DENIED_ERROR_CODE,
                e.getMessage(),
                org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e)
        );
    }

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handle(DomainException e) {
        logException(e);
        return new ErrorDTO(
                e.getErrorCode(),
                e.getMessage()
        );
    }

    @ExceptionHandler(ErrorCodedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handle(ErrorCodedException e) {
        logException(e);
        return new ErrorDTO(e.getErrorCode());
    }

    private void logException(Exception e) {
        LOG.error(
                String.format(
                        "Invoked exception handler for %s",
                        e.getClass()
                ),
                e
        );
    }
}