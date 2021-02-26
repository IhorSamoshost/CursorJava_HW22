package com.cursor.mongodb.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Bean
    public MongoDatabase mongoDatabase() {
        return mongoClient().getDatabase("my-db");
    }

    @Bean
    public MongoCollection<Document> mongoCollection() {
        return mongoDatabase().getCollection("my-alarms");
    }
}
