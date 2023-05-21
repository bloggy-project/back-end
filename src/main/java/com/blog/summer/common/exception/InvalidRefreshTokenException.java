package com.blog.summer.common.exception;

import com.blog.summer.common.dto.ResultType;

public class InvalidRefreshTokenException extends BaseException{

    public InvalidRefreshTokenException() {
        super(ResultType.INVALID_REFRESH_TOKEN);
    }

}
