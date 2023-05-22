package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ExceptionResult;
import com.blog.bloggy.common.dto.ResultType;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1733247474863885433L;

    private final ResultType resultType;

    public BaseException(ResultType resultType) {
        super(resultType.getMessage());
        this.resultType = resultType;
    }

    public BaseException(ResultType resultType, String message) {
        super(message);
        this.resultType = resultType;
    }

    public ExceptionResult getExceptionResult() {
        return new ExceptionResult(this.resultType);
    }
}