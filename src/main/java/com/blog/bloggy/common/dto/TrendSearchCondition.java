package com.blog.bloggy.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrendSearchCondition {
    private LocalDateTime time;

    private String date;

    private Long favorites;

    public TrendSearchCondition() {
    }

}
