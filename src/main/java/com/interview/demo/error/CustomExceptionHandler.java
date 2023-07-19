package com.interview.demo.error;


import com.interview.demo.model.wrapper.RespWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static String ERROR_CODE_AURGUMENT_ERROR = "ARGUMENT_ERROR";
    private static String ERROR_CODE_REQUEST_TIMEOUT = "REQUEST_TIMEOUT";
    private static String ERROR_CODE_NOT_FOUND = "NOT_FOUND";
    private static String ERROR_CODE_METHOD_NOT_SUPPORTED = "METHOD_NOT_SUPPORTED";
    private static String ERROR_CODE_MEDIA_TYPE_NOT_SUPPORTED = "MEDIA_TYPE_NOT_SUPPORTED";
    private static String ERROR_CODE_INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    private static String ERROR_CODE_MESSAGE_NOT_WRITABLE = "MESSAGE_NOT_WRITABLE";

    // 400
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<RespWrapper<ApiError>> handleBadRequestException(final BadRequestException ex, final WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    // 401, 403
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<RespWrapper<ApiError>> handleBadRequestException(final AccessDeniedException ex, final WebRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        ApiError apiError = null;
        if (isAuthenticated) {
            apiError = new ApiError(HttpStatus.FORBIDDEN, new ForbiddenRequestException(ApiErrorCode.FORBIDDEN));
        } else {
            apiError = new ApiError(HttpStatus.UNAUTHORIZED, new ForbiddenRequestException(ApiErrorCode.UNAUTHORIZED));
        }
        return new ResponseEntity<>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    // 401
    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<RespWrapper<ApiError>> handleBadRequestException(final UnauthorizedException ex, final WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex);
        return new ResponseEntity<>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    // 403
    @ExceptionHandler({ForbiddenRequestException.class})
    public ResponseEntity<RespWrapper<ApiError>> handleBadRequestException(final ForbiddenRequestException ex, final WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex);
        return new ResponseEntity<>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), errors, ERROR_CODE_AURGUMENT_ERROR);
        return new ResponseEntity<>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), errors, ERROR_CODE_AURGUMENT_ERROR);
        return new ResponseEntity<>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();

        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_AURGUMENT_ERROR);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getRequestPartName() + " part is missing";
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_AURGUMENT_ERROR);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getParameterName() + " parameter is missing";
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_AURGUMENT_ERROR);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getMessage();
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_AURGUMENT_ERROR);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getMessage();
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_AURGUMENT_ERROR);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getMessage();
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_AURGUMENT_ERROR);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getMessage();
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_AURGUMENT_ERROR);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getMessage();
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_AURGUMENT_ERROR);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getMessage();
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_MESSAGE_NOT_WRITABLE);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getMessage();
        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_REQUEST_TIMEOUT);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }


    // 404
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), error, ERROR_CODE_NOT_FOUND);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());

    }

    // 404
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<RespWrapper<ApiError>> handleResourceNotFound(final ResourceNotFoundException ex, final WebRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex);
        return new ResponseEntity<>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    // 405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), builder.toString(), ERROR_CODE_METHOD_NOT_SUPPORTED);
        return new ResponseEntity<Object>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    // 415
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

        final ApiError apiError = new ApiError(status, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2), ERROR_CODE_MEDIA_TYPE_NOT_SUPPORTED);
        return new ResponseEntity<>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        log.error("error", ex);
        //
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred", ERROR_CODE_INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }

    // 500
    @ExceptionHandler({Exception.class})
    public ResponseEntity<RespWrapper<ApiError>> handlInternalServerError(final Exception ex, final WebRequest request) {
        log.error("Unexpected internal server error", ex);
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, new InternalServerErrorException(ApiErrorCode.UNEXPECTED_ERROR, ex));
        return new ResponseEntity<>(RespWrapper.failure(apiError), new HttpHeaders(), apiError.getStatus());
    }
}