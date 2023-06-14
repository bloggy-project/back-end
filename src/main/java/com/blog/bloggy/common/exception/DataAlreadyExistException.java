package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class DataAlreadyExistException extends BaseException{
    public DataAlreadyExistException() {
        super(ResultType.DATA_ALREADY_EXIST);
    }

    public DataAlreadyExistException(ResultType resultType, String message) {
        super(ResultType.DATA_ALREADY_EXIST, message);
    }
}
