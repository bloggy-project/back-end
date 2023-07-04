package com.blog.bloggy.kafka.service;

import com.blog.bloggy.kafka.dto.Alarm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    @KafkaListener(
            groupId = "${kafka.consumer.group-id}",
            topics = "${kafka.topic.name}"
    )
    public void saveAlaram(Alarm alarm){
        return;
    }
}
