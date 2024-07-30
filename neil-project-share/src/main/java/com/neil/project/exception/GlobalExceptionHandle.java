package com.neil.project.exception;

import com.neil.project.common.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.neil.project.exception.ErrorCode.*;

/**
 * @author nihao
 * @date 2024/5/23
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BaseResult<Object> errorResult = buildErrorResult(HTTP_MESSAGE_NOT_READABLE, null);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    /**
     * 请求体无法被解析成目标对象
     */

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!CollectionUtils.isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
        }
        BaseResult<Void> errorResult = buildErrorResult(HTTP_METHOD_NOT_ALLOWED);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatusCode status,
                                                                     WebRequest request) {
        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }
        BaseResult<Void> errorResult = buildErrorResult(UNSUPPORTED_MEDIA_TYPE);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatusCode status,
                                                                      WebRequest request) {
        BaseResult<Void> errorResult = buildErrorResult(NOT_ACCEPTABLE);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatusCode status,
                                                               WebRequest request) {
        BaseResult<Void> errorResult = buildErrorResult(MISSING_PATH_VARIABLE);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          WebRequest request) {
        BaseResult<Void> errorResult = buildErrorResult(MISSING_SERVLET_REQUEST_PARAMETER);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          WebRequest request) {
        BaseResult<Void> errorResult = buildErrorResult(SERVLET_REQUEST_BINDING_EXCEPTION);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        BaseResult<Void> errorResult = buildErrorResult(CONVERSION_NOT_SUPPORTED);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers,
                                                        HttpStatusCode status,
                                                        WebRequest request) {
        BaseResult<Void> errorResult = buildErrorResult(TYPE_MISMATCH);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        BaseResult<Void> errorResult = buildErrorResult(HTTP_MESSAGE_NOT_WRITEABLE);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatusCode status,
                                                                     WebRequest request) {
        BaseResult<Void> errorResult = buildErrorResult(MISSING_SERVLET_REQUEST_PART);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatusCode status,
                                                                   WebRequest request) {
        BaseResult<Void> errorResult = buildErrorResult(NO_HANDLER_FOUND);
        return handleExceptionInternal(ex, errorResult, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex,
                                                                        HttpHeaders headers,
                                                                        HttpStatusCode status,
                                                                        WebRequest webRequest) {
        BaseResult<Void> errorResult = buildErrorResult(ASYNC_REQUEST_TIMEOUT);
        return handleExceptionInternal(ex, errorResult, headers, status, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getDefaultMessage());
        }

        BaseResult<List<Void>> errorResult = buildErrorResult(PARAM_ILLEGAL.getCode(), String.join(",", errors));

        return handleExceptionInternal(
                ex, errorResult, headers, status, request);
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<Object> bizExceptionHandler(BizException ex) {
        BaseResult<Void> errorResult = buildErrorResult(ex.getErrorCode(), ex.getErrorMsg());
        return new ResponseEntity<>(errorResult, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception ex) {
        log.error("exceptionHandler:", ex);
        BaseResult<Void> errorResult = buildErrorResult(SYS_ERROR);
        return new ResponseEntity<>(errorResult, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException ex) {
        BaseResult<Void> errorResult = BaseResult.fail(SYS_ERROR.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorResult, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private <T> BaseResult<T> buildErrorResult(ErrorCode errorCode, T result) {
        BaseResult<T> errorResult = new BaseResult<>();
        errorResult.setErrorMessage(errorCode.getErrorMsg());
        errorResult.setCode(errorCode.getCode());
        errorResult.setResult(result);
        return errorResult;
    }

    private BaseResult<Void> buildErrorResult(ErrorCode errorCode) {
        BaseResult<Void> errorResult = new BaseResult<>();
        errorResult.setErrorMessage(errorCode.getErrorMsg());
        errorResult.setCode(errorCode.getCode());
        return errorResult;
    }

    protected <T> BaseResult<T> buildErrorResult(String errorCode, String errorMsg) {
        BaseResult<T> errorResult = new BaseResult<>();
        errorResult.setErrorMessage(errorMsg);
        errorResult.setCode(errorCode);
        return errorResult;
    }

}
