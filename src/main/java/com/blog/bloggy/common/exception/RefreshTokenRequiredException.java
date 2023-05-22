package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class RefreshTokenRequiredException extends BaseException {

    private static final long serialVersionUID = 5600656707384112141L;

    public RefreshTokenRequiredException(String message) {
        super(ResultType.REFRESH_TOKEN_REQUIRED, message);
    }

    public RefreshTokenRequiredException() {
        super(ResultType.REFRESH_TOKEN_REQUIRED);
    }
}