package com.quiz.global.db.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addObject(String key, Object value, Long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MILLISECONDS);
    }

    public Optional<Long> getObject(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        Optional<Long> result;
        if (value == null) {
            result = Optional.empty();
        } else {
            result = Optional.of(Long.parseLong(key));
        }
        return result;
    }

    public void deleteObject(String key) {
        redisTemplate.delete(key);
    }
}
