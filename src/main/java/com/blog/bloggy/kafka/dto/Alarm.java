package com.blog.bloggy.kafka.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
    private int id;
    private String sender;
    private String content;
    private LocalDateTime sentAt;
}
