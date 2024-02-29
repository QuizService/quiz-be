package com.quiz;

import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

@Configuration
@EnableMongoRepositories
public class MongoDbTestConfig {
    private static final int port = 27016;

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withExposedPorts(port);

    static {
        mongoDBContainer.start();
        Integer mappedPort = mongoDBContainer.getMappedPort(port);
        System.setProperty("mongodb.container.port", String.valueOf(mappedPort));
    }

}
