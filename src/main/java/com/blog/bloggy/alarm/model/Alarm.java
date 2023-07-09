package com.blog.bloggy.alarm.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue
    @Column(name = "alarm_id")
    private Long id;
    private String sender;
    private AlarmTypes alarmTypes;
    private LocalDateTime sentAt;

}
