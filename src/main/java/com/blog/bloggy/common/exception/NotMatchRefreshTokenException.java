package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class NotMatchRefreshTokenException extends BaseException{

    public NotMatchRefreshTokenException() {
        super(ResultType.NOT_MATCH_REFRESH_TOKEN);
    }

}
