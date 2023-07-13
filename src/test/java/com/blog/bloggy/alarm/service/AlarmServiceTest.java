package com.blog.bloggy.alarm.service;

import com.blog.bloggy.alarm.model.Alarm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class AlarmServiceTest {

    @Autowired
    AlarmService alarmService;
    @Test
    @DisplayName("알람 1개 전송 확인")
    @Transactional
    void alarmServiceTest() throws InterruptedException {
        sendOne();
        Thread.sleep(2000);
    }
    @Test
    @DisplayName("알람 여러개 전송 확인")
    @Transactional
    void alarmServiceMultiTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 500; i++) {
            executorService.submit(()->sendOne());
        }

        executorService.shutdown();

    }

    private void sendOne() {
        String username= "TestUser";
        Alarm alarm = Alarm.createAlarm(username);
        alarmService.sendChat(alarm);
    }
}