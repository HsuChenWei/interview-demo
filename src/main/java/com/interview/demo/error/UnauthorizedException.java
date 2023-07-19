package com.interview.demo.error;

public class UnauthorizedException extends RestApiException {

    public UnauthorizedException(ApiErrorCode apiErrorCode) {
        super(apiErrorCode);
        // TODO Auto-generated constructor stub
    }

    public UnauthorizedException(ApiErrorCode apiErrorCode, String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(apiErrorCode, message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public UnauthorizedException(ApiErrorCode apiErrorCode, String message, Throwable cause) {
        super(apiErrorCode, message, cause);
        // TODO Auto-generated constructor stub
    }

    public UnauthorizedException(ApiErrorCode apiErrorCode, String message) {
        super(apiErrorCode, message);
        // TODO Auto-generated constructor stub
    }

    public UnauthorizedException(ApiErrorCode apiErrorCode, Throwable cause) {
        super(apiErrorCode, cause);
        // TODO Auto-generated constructor stub
    }

}
