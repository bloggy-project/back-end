package com.blog.bloggy.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

import static com.blog.bloggy.common.config.CacheExpireConfig.POST_CACHE_EXPIRE_TIME;
import static com.blog.bloggy.common.config.CacheKeyConfig.POST;

@Configuration
@EnableCaching
@EnableRedisRepositories(basePackages = {"com.blog.bloggy.token.repository", "com.blog.bloggy.post.redisRepository"})
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean(name = "redisCacheConnectionFactory")
    public RedisConnectionFactory redisCacheConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    public RedisTemplate<String, Object> responsePostDtoRedisTemplate(
            @Qualifier("redisCacheConnectionFactory") RedisConnectionFactory redisConnectionFactory
    ){
        ObjectMapper mapper = getObjectMapper();
        GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(mapper);

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(redisSerializer);

        return redisTemplate;
    }

    @Bean
    public RedisCacheManager cacheManager(
            @Qualifier("redisCacheConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        ObjectMapper mapper = getObjectMapper();

        GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer(mapper);

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(redisSerializer))
                .entryTtl(POST_CACHE_EXPIRE_TIME);
        Map<String, RedisCacheConfiguration> configurations = new HashMap<>();

        configurations.put(POST, cacheConfiguration.entryTtl(POST_CACHE_EXPIRE_TIME));

        return RedisCacheManager.builder(redisConnectionFactory)
                .withInitialCacheConfigurations(configurations)
                .build();
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator
                        .builder()
                        .allowIfSubType(Object.class)   //모든 객체의 타입정보를 저장할 수 있도록 설정
                        .build(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );
        mapper.registerModule(new JavaTimeModule());    //LocaldateTime 저장을 위해 등록
        mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);	//LocaldateTime을 Day까지 반환해줌
        return mapper;
    }
}
