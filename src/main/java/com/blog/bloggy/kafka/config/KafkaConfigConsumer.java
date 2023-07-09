package com.blog.bloggy.kafka.config;

import com.blog.bloggy.alarm.model.Alarm;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfigConsumer {
    @Value("${kafka.bootstrap.address}")
    private String bootStrapAddress;

    @Value("${kafka.consumer.group-id}")
    private String groupId;
    @Bean
    public Map<String, Object> kafkaConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return props;
    }

    @Bean
    public ConsumerFactory<String, Alarm> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfig(),
                new StringDeserializer(), new JsonDeserializer<>(Alarm.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Alarm> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Alarm> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory());
        //factory.setBatchListener(true);
        //factory.setBatchErrorHandler(new RecoveringBatchErrorHandler());
        return factory;
    }
}
