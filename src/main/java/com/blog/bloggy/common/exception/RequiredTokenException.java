package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class RequiredTokenException extends BaseException {

    private static final long serialVersionUID = 5600656707384112141L;

    public RequiredTokenException(String message) {
        super(ResultType.REQUIRED_TOKEN, message);
    }
    public RequiredTokenException() {
        super(ResultType.REQUIRED_TOKEN);
    }

}