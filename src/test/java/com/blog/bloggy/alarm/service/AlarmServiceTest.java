package com.blog.bloggy.alarm.service;

import com.blog.bloggy.alarm.model.Alarm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class AlarmServiceTest {

    @Autowired
    AlarmService alarmService;
    @Test
    @DisplayName("알람 전송 확인")
    @Transactional
    void alarmServiceTest() {
        String username= "TestUser";
        Alarm alarm = Alarm.createAlarm(username);
        alarmService.sendChat(alarm);

    }
}