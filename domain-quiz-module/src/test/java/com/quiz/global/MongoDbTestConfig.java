//package com.quiz.global;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.MongoDBContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//@TestConfiguration
//@EnableMongoRepositories(basePackages = "com.quiz.domain.*.mongo")
//public class MongoDbTestConfig {
//    private static final int port = 27017;
//
//    @Value("spring.data.mongodb.username")
//    private static String username;
//
//    @Value("spring.data.mongodb.password")
//    private static String password;
//
//    @Value("spring.data.mongodb.database")
//    private static String database;
//
//
//
//
//}
