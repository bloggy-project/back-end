package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class InvalidTokenTypeException extends BaseException{

    public InvalidTokenTypeException() {
        super(ResultType.INVALID_TOKEN_TYPE);
    }


    public InvalidTokenTypeException(String message) {
        super(ResultType.INVALID_TOKEN_TYPE, message);
    }
}
