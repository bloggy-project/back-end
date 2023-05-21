package com.blog.summer.exhandler.advice;

import com.blog.summer.common.dto.ExceptionResult;
import com.blog.summer.common.dto.ResultType;
import com.blog.summer.common.exception.BaseException;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> notFoundExHandler(NotFoundException e){
        log.error("[notFoundExHandler] ex", e);
        ErrorResult result = new ErrorResult("NotFound", e.getMessage());
        return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
    }


    // customException 발생한 경우
    @ExceptionHandler({BaseException.class})
    public ExceptionResult handleBaseException(BaseException ex) {
        log.error("handleBaseException ex :::", ex);
        return ex.getExceptionResult();
    }
    //테스트

    private ExceptionResult processException(ResultType resultType) {
        return new ExceptionResult(resultType);
    }
}

