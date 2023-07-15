package com.blog.bloggy.alarm.model;


import com.blog.bloggy.alarm.dto.AlarmDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static com.blog.bloggy.alarm.model.AlarmTypes.FRIEND_CREATE_POST;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Alarm {

    @Id
    private Long id;
    private String sender;
    private String message;
    private LocalDateTime sentAt;

    public static Alarm createAlarm(String name){
        return Alarm.builder()
                .sender(name)
                .message(name+FRIEND_CREATE_POST.getMessage())
                .sentAt(LocalDateTime.now())
                .build();
    }
}
