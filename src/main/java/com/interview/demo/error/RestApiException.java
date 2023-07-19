package com.interview.demo.error;

public abstract class RestApiException extends RuntimeException {
    private ApiErrorCode apiErrorCode;

    public RestApiException(ApiErrorCode apiErrorCode) {
        this(apiErrorCode, apiErrorCode.toString());


    }

    public RestApiException(ApiErrorCode apiErrorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.apiErrorCode = apiErrorCode;
    }

    public RestApiException(ApiErrorCode apiErrorCode, String message, Throwable cause) {
        super(message, cause);
        this.apiErrorCode = apiErrorCode;
    }

    public RestApiException(ApiErrorCode apiErrorCode, String message) {
        super(message);
        this.apiErrorCode = apiErrorCode;
    }

    public RestApiException(ApiErrorCode apiErrorCode, Throwable cause) {
        super(cause);
        this.apiErrorCode = apiErrorCode;
    }

    public ApiErrorCode getApiErrorCode() {
        return apiErrorCode;
    }

    public void setApiErrorCode(ApiErrorCode apiErrorCode) {
        this.apiErrorCode = apiErrorCode;
    }

}
