package com.quiz.global.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TxConfig {
    @Bean("chainedTransactionManager")
    public PlatformTransactionManager chainedTransactionManager
            (@Qualifier("mongoTx") MongoTransactionManager mongoTransactionManager,
             @Qualifier("mysqlTransactionManager") JpaTransactionManager jpaTransactionManager  ){
        ChainedTransactionManager transactionManager = new ChainedTransactionManager(
                jpaTransactionManager, mongoTransactionManager
        );
        return transactionManager;
    }
}
