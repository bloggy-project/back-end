package com.blog.summer.common.exception;

import com.blog.summer.common.dto.ResultType;

public class ExpiredTokenException extends BaseException {

    private static final long serialVersionUID = -6564973261837433080L;

    public ExpiredTokenException() {
        super(ResultType.EXPIRED_TOKEN);
    }
}
