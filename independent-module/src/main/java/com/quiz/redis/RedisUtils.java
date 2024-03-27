package com.quiz.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public void deleteRange(String key, Long start, Long end) {
        ZSetOperations<String, Object> zSetOperations = opsForZSet();
        zSetOperations.removeRange(key, start, end);
    }

    public ZSetOperations<String, Object> opsForZSet() {
        return redisTemplate.opsForZSet();
    }

    public void addQueue(String key, Object value, Long score) {
        try {
            opsForZSet().add(key, value, (double) score);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Long getZSetSize(String str) {
        ZSetOperations<String, Object> z = redisTemplate.opsForZSet();
        return z.size(str);
    }

    public Set<Object> zRange(String key, Long start, Long end) {
        return opsForZSet().range(key, start, end);
    }

    public Long getZRank(String key, Object value) {
        return opsForZSet().rank(key, value);
    }

    public void setValue(String key, Long value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

}
