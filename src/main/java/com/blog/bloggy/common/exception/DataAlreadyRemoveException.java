package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class DataAlreadyRemoveException extends BaseException{
    public DataAlreadyRemoveException() {
        super(ResultType.DATA_ALREADY_REMOVE);
    }

    public DataAlreadyRemoveException(ResultType resultType, String message) {
        super(ResultType.DATA_ALREADY_REMOVE, message);
    }
}
