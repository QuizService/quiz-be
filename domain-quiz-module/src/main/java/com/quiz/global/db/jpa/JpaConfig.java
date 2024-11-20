package com.quiz.global.db.jpa;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
        transactionManagerRef = "mysqlTx",
        basePackages = "com.quiz.domain.users.repository")
@EnableTransactionManagement
public class JpaConfig {

    @Primary
    @Bean(name = "mysqlTx")
    public PlatformTransactionManager mysqlTransactionManager() {
        return new JpaTransactionManager();
    }
}
