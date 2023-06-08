package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class PostNotFoundException extends BaseException{
    public PostNotFoundException() {
        super(ResultType.POST_NOT_FOUND);
    }

    public PostNotFoundException(ResultType resultType, String message) {
        super(ResultType.POST_NOT_FOUND, message);
    }
}
