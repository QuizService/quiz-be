package com.quiz.domain.responses;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadTest {
    int numberOfTask = 10000;

    void work(int taskNum, ExecutorService executor) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(taskNum);
        for (int i = 0; i < taskNum; i++) {
            int num = i;
            executor.execute(() -> {
                try {
                    Thread.sleep(10);
                    System.out.println("thread - " + num);
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        countDownLatch.await();
        ;
    }

    @Test
    void testThread() throws InterruptedException {

        long startTime;

        ExecutorService executor = Executors.newFixedThreadPool(32);
        startTime = System.nanoTime();
        work(numberOfTask, executor);
        long duration = System.nanoTime() - startTime;
        executor.shutdown();
        //taskNum = 100 -> 75795200
        //taskNum = 1000 -> 446492700
        //taskNum = 10000 -> 4393024000
    }

    @Test
    void testVirtualThread() throws InterruptedException {
        long startTime;

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        startTime = System.nanoTime();
        work(numberOfTask, executor);
        long duration = System.nanoTime() - startTime;
        executor.shutdown();
        //taskNum = 100 -> 31272000
        //taskNum = 1000 -> 219035200
        //taskNum = 10000 -> 1363350400
    }
}
