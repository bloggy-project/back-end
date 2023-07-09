package com.blog.bloggy.alarm.dto;

import com.blog.bloggy.alarm.model.AlarmTypes;
import lombok.*;

import java.time.LocalDateTime;

import static com.blog.bloggy.alarm.model.AlarmTypes.FRIEND_CREATE_POST;


@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {

    private String sender;
    private String message;
    private LocalDateTime sentAt;


    public static AlarmDto createAlarm(String name){
        return AlarmDto.builder()
                .sender(name)
                .message(name+FRIEND_CREATE_POST.getMessage())
                .sentAt(LocalDateTime.now())
                .build();
    }
}
