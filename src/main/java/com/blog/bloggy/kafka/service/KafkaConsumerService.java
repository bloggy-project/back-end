package com.blog.bloggy.kafka.service;

import com.blog.bloggy.alarm.dto.AlarmDto;
import com.blog.bloggy.alarm.model.Alarm;
import com.blog.bloggy.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final AlarmRepository alarmRepository;

    @KafkaListener(
            groupId = "${kafka.consumer.group-id}",
            topics = "${kafka.topic.name}"
    )
    public void saveAlaram(List<AlarmDto> alarmDtos) {
        List<Alarm> alarms=alarmDtos.stream().map((alarmDto -> {
            return Alarm.builder()
                    .sender(alarmDto.getSender())
                    .sentAt(alarmDto.getSentAt())
                    .alarmTypes(alarmDto.getAlarmTypes())
                    .build();
                })).collect(toList());

        alarmRepository.saveAll(alarms);
    }
}
