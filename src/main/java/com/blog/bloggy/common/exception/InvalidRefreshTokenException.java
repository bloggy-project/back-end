package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class InvalidRefreshTokenException extends BaseException{

    public InvalidRefreshTokenException() {
        super(ResultType.INVALID_REFRESH_TOKEN);
    }

}
