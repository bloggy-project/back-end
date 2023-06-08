package com.blog.bloggy.common.exception;

import com.blog.bloggy.common.dto.ResultType;

public class CommentNotFoundException extends BaseException{
    public CommentNotFoundException() {
        super(ResultType.COMMENT_NOT_FOUND);
    }

    public CommentNotFoundException(String message) {
        super(ResultType.COMMENT_NOT_FOUND, message);
    }
}
