package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class UserNotFoundException extends BaseException{
    public UserNotFoundException() {
        super(ResultType.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(ResultType.USER_NOT_FOUND, message);
    }
}
