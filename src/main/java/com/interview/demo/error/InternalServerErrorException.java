package com.interview.demo.error;

public class InternalServerErrorException extends RestApiException {
    public InternalServerErrorException(ApiErrorCode apiErrorCode) {
        super(apiErrorCode);
    }

    public InternalServerErrorException(ApiErrorCode apiErrorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(apiErrorCode, message, cause, enableSuppression, writableStackTrace);
    }

    public InternalServerErrorException(ApiErrorCode apiErrorCode, String message, Throwable cause) {
        super(apiErrorCode, message, cause);
    }

    public InternalServerErrorException(ApiErrorCode apiErrorCode, String message) {
        super(apiErrorCode, message);
    }

    public InternalServerErrorException(ApiErrorCode apiErrorCode, Throwable cause) {
        super(apiErrorCode, cause);
    }
}
