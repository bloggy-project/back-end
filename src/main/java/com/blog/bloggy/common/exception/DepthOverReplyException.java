package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class DepthOverReplyException extends BaseException{
    public DepthOverReplyException() {
        super(ResultType.DEPTH_OVER_REPLY);
    }

    public DepthOverReplyException(String message) {
        super(ResultType.DEPTH_OVER_REPLY, message);
    }
}
