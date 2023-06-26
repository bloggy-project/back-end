package com.blog.bloggy.common.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TrendSearchCondition {
    private Long lastId;

    private String date;

    private Long favorCount;


}
