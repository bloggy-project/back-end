package com.blog.bloggy.alarm.dto;

import com.blog.bloggy.alarm.model.AlarmTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {

    private String sender;
    private AlarmTypes alarmTypes;
    private LocalDateTime sentAt;
}
