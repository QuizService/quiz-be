package com.quiz.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    TimeUnit timeunit() default TimeUnit.SECONDS;

    // 락을 기다리는 시간, 락 확득을 위해 waitTime 만큼 대기
    long waitTime() default 5L;

    // 락 임대 시간, 락 획득 후 leaseTime이 지나면 락을 해제
    long leaseTime() default 3L;
}
