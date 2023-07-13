package com.blog.bloggy.alarm.service;

import com.blog.bloggy.alarm.dto.AlarmDto;
import com.blog.bloggy.alarm.model.Alarm;
import com.blog.bloggy.kafka.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final KafkaProducerService kafkaProducerService;

    @Value("${spring.kafka.topic.name}")
    private String kafkaTopic;

    @Transactional
    public void sendChat(Alarm alarm) {
        log.info("알람 서비스 메시지 발행: {}", alarm);
        kafkaProducerService.deliverAlarmToKafka(kafkaTopic, alarm);

    }

}
