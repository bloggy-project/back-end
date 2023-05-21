package com.blog.summer.exception;

import com.blog.summer.exception.message.ExceptionMessage;

public class TokenCheckFailException extends RuntimeException{


    public TokenCheckFailException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.message());
    }

}
