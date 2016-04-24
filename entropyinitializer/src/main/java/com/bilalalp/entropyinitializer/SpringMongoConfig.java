package com.bilalalp.entropyinitializer;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.bilalalp.entropyinitializer.repo")
@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "tfidf";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient("127.0.0.1");
    }
}