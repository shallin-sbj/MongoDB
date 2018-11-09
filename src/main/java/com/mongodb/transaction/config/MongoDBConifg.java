package com.mongodb.transaction.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoDBConifg {

    private MongoClient client;

    @Value("${spring.data.mongodb.uri}")
    private String mongodbURI;
    @Value("${spring.data.mongodb.dbname}")
    private String databaseName;

    private MongoDatabase mongoDatabase;

    @PostConstruct
    public void init() {
        getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        CodecRegistry codecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(
                PojoCodecProvider.builder().register("com.mongodb.models").build()));
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientOptions.Builder options = new MongoClientOptions.Builder().codecRegistry(pojoCodecRegistry);
        MongoClientURI uri = new MongoClientURI(mongodbURI, options);
        client = new MongoClient(uri);
        mongoDatabase = client.getDatabase(databaseName);
        mongoDatabase.withCodecRegistry(codecRegistry);
    }

    public MongoClient getClient() {
        return client;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
