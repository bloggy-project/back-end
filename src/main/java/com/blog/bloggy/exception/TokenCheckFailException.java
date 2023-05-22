package com.blog.bloggy.exception;

import com.blog.bloggy.exception.message.ExceptionMessage;

public class TokenCheckFailException extends RuntimeException{


    public TokenCheckFailException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.message());
    }

}
