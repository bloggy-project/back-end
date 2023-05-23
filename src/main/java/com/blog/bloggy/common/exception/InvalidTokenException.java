package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class InvalidTokenException extends BaseException{

    public InvalidTokenException() {
        super(ResultType.INVALID_TOKEN);
    }
    public InvalidTokenException(String message) {
        super(ResultType.INVALID_TOKEN, message);
    }

}
