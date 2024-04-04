package com.quiz.global.db.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableMongoRepositories(
        basePackages = {"com.quiz.domain.*.repository.mongo"})
@EnableTransactionManagement
public class MongoDBConfig extends AbstractMongoClientConfiguration {
    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;


    @Bean(name = "mongoTx")
    public PlatformTransactionManager transactionManager(@Qualifier("mongoDbFactory") MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTransactionManager(mongoDatabaseFactory);
    }

    @Bean(name = "mongoDbFactory")
    @Override
    public MongoDatabaseFactory mongoDbFactory() {
        return super.mongoDbFactory();
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(this.connectionString);

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyToConnectionPoolSettings(builder -> builder
                        .maxConnectionIdleTime(10, TimeUnit.SECONDS))
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), databaseName);
    }
}
