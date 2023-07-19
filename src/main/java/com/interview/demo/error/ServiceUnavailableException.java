package com.interview.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException extends RestApiException {

    public ServiceUnavailableException(ApiErrorCode apiErrorCode, String message, Throwable cause,
                                       boolean enableSuppression, boolean writableStackTrace) {
        super(apiErrorCode, message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public ServiceUnavailableException(ApiErrorCode apiErrorCode, String message, Throwable cause) {
        super(apiErrorCode, message, cause);
        // TODO Auto-generated constructor stub
    }

    public ServiceUnavailableException(ApiErrorCode apiErrorCode, String message) {
        super(apiErrorCode, message);
        // TODO Auto-generated constructor stub
    }

    public ServiceUnavailableException(ApiErrorCode apiErrorCode, Throwable cause) {
        super(apiErrorCode, cause);
        // TODO Auto-generated constructor stub
    }

    public ServiceUnavailableException(ApiErrorCode apiErrorCode) {
        super(apiErrorCode);
        // TODO Auto-generated constructor stub
    }

}
