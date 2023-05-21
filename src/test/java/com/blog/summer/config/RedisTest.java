package com.blog.summer.config;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    @Test
    public void redisConnectionTest() throws Exception {
        //given
        final String key = "a";
        final String data = "1";
        //when
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, data);
        //then
        String s = valueOperations.get(key);
        Assertions.assertThat(s).isEqualTo(data);
    }
}
