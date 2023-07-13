package com.blog.bloggy.kafka.service;

import com.blog.bloggy.alarm.model.Alarm;
import com.blog.bloggy.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;



@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final AlarmRepository alarmRepository;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.name}"
    )
    public void saveAlaram(List<Alarm> alarms) {
        alarms.stream()
                .forEach(alarm -> log.info("saveAlarm: {}", alarm));


        alarmRepository.saveAll(alarms);
    }
}
