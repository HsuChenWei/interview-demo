package com.interview.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RestApiException {
    public ResourceNotFoundException(ApiErrorCode apiErrorCode) {
        super(apiErrorCode);
        // TODO Auto-generated constructor stub
    }

    public ResourceNotFoundException(ApiErrorCode apiErrorCode, String message, Throwable cause,
                                     boolean enableSuppression, boolean writableStackTrace) {
        super(apiErrorCode, message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public ResourceNotFoundException(ApiErrorCode apiErrorCode, String message, Throwable cause) {
        super(apiErrorCode, message, cause);
        // TODO Auto-generated constructor stub
    }

    public ResourceNotFoundException(ApiErrorCode apiErrorCode, String message) {
        super(apiErrorCode, message);
        // TODO Auto-generated constructor stub
    }

    public ResourceNotFoundException(ApiErrorCode apiErrorCode, Throwable cause) {
        super(apiErrorCode, cause);
        // TODO Auto-generated constructor stub
    }

}
