package com.quiz.global.testContainer;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestContainerConfig implements BeforeAllCallback {
    private DockerComposeContainer<?> dockerCompose;
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        dockerCompose = new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                .withExposedService("redis1", 6379)
                .withExposedService("redis2", 6380);

        dockerCompose.start();

        System.setProperty("spring.data.redis.host1", "localhost");
        System.setProperty("spring.data.redis.port1", String.valueOf(6379));
        System.setProperty("spring.data.redis.host2", "localhost");
        System.setProperty("spring.data.redis.port2", String.valueOf(6380));
    }
}
