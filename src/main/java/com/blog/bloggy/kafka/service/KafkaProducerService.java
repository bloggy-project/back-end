package com.blog.bloggy.kafka.service;

import com.blog.bloggy.kafka.dto.Alarm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Alarm> kafkaTemplate;

    public void deliverAlarmToKafka(String topic, Alarm alarm) {
        ListenableFuture<SendResult<String, Alarm>> sendResult = kafkaTemplate.send(topic, alarm);
        sendResult.addCallback(new ListenableFutureCallback<SendResult<String, Alarm>>() {
            @Override
            public void onSuccess(SendResult<String, Alarm> result) {
                log.info("successfully sent a message {} with offset {}", alarm, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("fail to send a message {} because of {}", alarm, ex.getMessage());
                throw new MessagingException("fail to send a message", ex);
            }
        });
    }
}
