package com.quiz.global.db.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Redis2Utils {
    private final RedisTemplate<String, Object> redisTemplate;

    public Redis2Utils(@Qualifier("redisTemplate2") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addObject(Long key, String value, Long time) {
        redisTemplate.opsForValue().set(String.valueOf(key), value, time, TimeUnit.MILLISECONDS);
    }

    public Optional<String> getObject(Long key) {
        Object value = redisTemplate.opsForValue().get(String.valueOf(key));
        Optional<String> result;
        if(value == null) {
            result = Optional.empty();
        } else {
            result = Optional.ofNullable(value.toString());
        }
        return result;
    }

    public void deleteObject(Long key) {
        redisTemplate.delete(String.valueOf(key));
        log.info("key deleted : {}", key);
    }
}
