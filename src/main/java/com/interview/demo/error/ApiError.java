package com.interview.demo.error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ApiError {
    private HttpStatus status;
    private String apiErrorCode;
    private String message;
    private List<String> errors;


    public ApiError() {
        super();
    }


    public ApiError(final HttpStatus status, final RestApiException ex) {
        this(status, ex.getLocalizedMessage(),ex.toString());
        this.apiErrorCode = ex.getApiErrorCode().toString();
    }
    public ApiError(final HttpStatus status, final String message, final List<String> errors, final String apiErrorCode) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.apiErrorCode = apiErrorCode;
    }
    public ApiError(final HttpStatus status, final String message, final List<String> errors) {
        this(status, message, errors, null);
    }
    public ApiError(final HttpStatus status, final String message, final String error) {
        this(status, message, Arrays.asList(error));
    }

    public ApiError(final HttpStatus status, final String message, final String error, final String apiErrorCode) {
        this(status, message, Arrays.asList(error), apiErrorCode);
    }




}
