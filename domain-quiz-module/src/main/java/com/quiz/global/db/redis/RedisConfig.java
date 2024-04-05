package com.quiz.global.db.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableRedisRepositories(basePackages = {"com.quiz.global.db.redis"},
        redisTemplateRef = "redisTemplate1")
public class RedisConfig {
    @Value("${spring.data.redis.host1}")
    private String host1;

    @Value("${spring.data.redis.port1}")
    private int port1;

    @Primary
    @Bean(name = "redisConnectionFactory1")
    public RedisConnectionFactory redisConnectionFactory1() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host1, port1));
    }

    @Primary
    @Bean(name = "redisTemplate1")
    public RedisTemplate<String, Object> redisTemplate1() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory1());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean(name = "redisTx1")
    public PlatformTransactionManager transactionManager1() {
        return new JpaTransactionManager();
    }

}
