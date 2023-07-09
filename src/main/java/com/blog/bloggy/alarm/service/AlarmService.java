package com.blog.bloggy.alarm.service;

import com.blog.bloggy.alarm.dto.AlarmDto;
import com.blog.bloggy.alarm.model.Alarm;
import com.blog.bloggy.common.exception.UserNotFoundException;
import com.blog.bloggy.kafka.service.KafkaProducerService;
import com.blog.bloggy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {


    private final UserRepository userRepository;

    private final KafkaProducerService kafkaProducerService;
    @Value("${kafka.topic.name}")
    private String kafkaTopic;

    @Transactional
    public void sendChat(AlarmDto alarmDto) {
        String username = alarmDto.getSender();
        userRepository.findByName(username).orElseThrow(UserNotFoundException::new);

        kafkaProducerService.deliverAlarmToKafka(kafkaTopic, alarmDto);

    }

}
