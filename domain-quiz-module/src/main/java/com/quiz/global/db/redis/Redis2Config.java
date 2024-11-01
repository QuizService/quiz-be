package com.quiz.global.db.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableRedisRepositories(basePackages = {"com.quiz.global.db.redis"},
        redisTemplateRef = "redisTemplate2")
public class Redis2Config {
    @Value("${spring.data.redis.host2}")
    private String host2;

    @Value("${spring.data.redis.port2}")
    private int port2;

    @Bean(name = "redisConnectionFactory2")
    public RedisConnectionFactory redisConnectionFactory2() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host2, port2));
    }

    @Bean(name = "redisTemplate2")
    public RedisTemplate<String, Object> redisTemplate2(@Qualifier("redisConnectionFactory2") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
