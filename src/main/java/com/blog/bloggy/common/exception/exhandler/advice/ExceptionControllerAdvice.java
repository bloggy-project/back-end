package com.blog.bloggy.common.exception.exhandler.advice;

import com.blog.bloggy.common.dto.ExceptionResult;
import com.blog.bloggy.common.dto.ResultType;
import com.blog.bloggy.common.exception.BaseException;
import com.blog.bloggy.common.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Iterator;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private void printException(Exception e, WebRequest request) {
        log.error("##################################################################################################");
        log.error("URL : {}", ((ServletWebRequest) request).getRequest().getRequestURI());
        log.error("Method : {}", ((ServletWebRequest) request).getHttpMethod());
        log.error("Headers : ");
        Iterator<String> headers = request.getHeaderNames();
        while (headers.hasNext()) {
            String header = headers.next();
            log.error("{} - {}", header, request.getHeader(header));
        }
        log.error("Parameters : ");
        Iterator<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasNext()) {
            String parameterName = parameterNames.next();
            log.error("{} - {}", parameterName, request.getParameter(parameterName));
        }
        log.error("Exception!!", e);
        log.error("##################################################################################################");
    }

    @ExceptionHandler({BaseException.class})
    public ResponseEntity<ErrorResult> handleBaseException(BaseException e) {
        log.error("handleBaseException ex :::", e);

        return new ResponseEntity(e.getExceptionResult(),e.getExceptionResult().getStatus());
    }

    //요청된 URL에 해당하는 핸들러가 없을 때 호출되는 메서드
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status,
                                                                   WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.PAGE_NOT_FOUND),
                ResultType.PAGE_NOT_FOUND.getStatus());
    }

    // 잘못된 HTTP 메서드가 요청되었을 때 호출되며, 메서드 허용되지 않음 응답을 반환
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.METHOD_NOT_ALLOWED),
                ResultType.METHOD_NOT_ALLOWED.getStatus());
    }

    // 필수 파라미터가 누락되었을 때 호출되며, 누락된 파라미터 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.MISSING_PARAMETER),
                ResultType.MISSING_PARAMETER.getStatus());
    }

    // 요청된 메서드의 매개변수 유효성 검사에 실패했을 때 호출되며, 유효성 검사 실패 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.INVALID_PARAMETER),
                ResultType.INVALID_PARAMETER.getStatus());
    }
    // 지원되지 않는 미디어 타입이 요청되었을 때 호출되며, 지원되지 않는 미디어 타입 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.UNSUPPORTED_MEDIA_TYPE),
                ResultType.UNSUPPORTED_MEDIA_TYPE.getStatus());
    }
    // 요청된 미디어 타입이 수락할 수 없을 때 호출되며, 미디어 타입 수락 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers, HttpStatus status,
                                                                      WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.UNSUPPORTED_MEDIA_TYPE),
                ResultType.UNSUPPORTED_MEDIA_TYPE.getStatus());
    }
    // 경로 변수가 누락되었을 때 호출되며, 누락된 경로 변수 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.MISSING_PARAMETER),
                ResultType.MISSING_PARAMETER.getStatus());
    }
    // 서블릿 요청 바인딩 오류가 발생했을 때 호출되며, 요청 바인딩 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.INVALID_PARAMETER),
                ResultType.INVALID_PARAMETER.getStatus());
    }
    // 변환 작업이 지원되지 않을 때 호출되며, 변환 작업 지원 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.NOT_ALLOWED_OPERATION),
                ResultType.NOT_ALLOWED_OPERATION.getStatus());
    }
    // 유형 불일치가 발생했을 때 호출되며, 유형 불일치 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers, HttpStatus status,
                                                        WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.INVALID_PARAMETER),
                ResultType.INVALID_PARAMETER.getStatus());
    }
    // 읽을 수 없는 HTTP 메시지가 전송되었을 때 호출되며, 읽을 수 없는 메시지 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.INVALID_PARAMETER),
                ResultType.INVALID_PARAMETER.getStatus());
    }
    // 쓸 수 없는 HTTP 메시지가 전송되었을 때 호출되며, 쓸 수 없는 메시지 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.NOT_ALLOWED_OPERATION),
                ResultType.NOT_ALLOWED_OPERATION.getStatus());
    }
    // 누락된 서블릿 요청 파트가 있을 때 호출되며, 누락된 요청 파트 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.INVALID_PARAMETER),
                ResultType.INVALID_PARAMETER.getStatus());
    }
    // 바인딩 오류가 발생했을 때 호출되며, 바인딩 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers, HttpStatus status,
                                                         WebRequest request) {
        this.printException(ex, request);

        return new ResponseEntity<>(new ExceptionResult(ResultType.INVALID_PARAMETER),
                ResultType.INVALID_PARAMETER.getStatus());
    }
    // 비동기 요청 시간 초과가 발생했을 때 호출되며, 서비스 이용 불가능 오류 응답을 반환
    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex,
                                                                        HttpHeaders headers, HttpStatus status,
                                                                        WebRequest webRequest) {
        this.printException(ex, webRequest);

        return new ResponseEntity<>(new ExceptionResult(ResultType.SERVICE_UNAVAILABLE),
                ResultType.SERVICE_UNAVAILABLE.getStatus());
    }

}

