package com.example.elasticService.databaseService.services;

import com.example.elasticService.databaseService.models.SQLiteConnectionEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.stereotype.Service;

@Service
public class MongoDBService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void storePayloadInMongoDB(SQLiteConnectionEntity connection, Map<String, Object> payload) throws Exception {
        // Parse connection string from SQLiteConnectionEntity
        Map<String, Object> connectionDetails = objectMapper.readValue(connection.getConnectionString(), Map.class);
//        System.out.println("ConnectionDetails" + connectionDetails);

        String host = (String) connectionDetails.get("host");
        int port = (int) connectionDetails.get("port");
        String username = (String) connectionDetails.get("username");
        String password = (String) connectionDetails.get("password");
        String databaseName = "payload_db"; // Default database name, can be dynamic
        String collectionName = "movies"; // Default collection name, can be dynamic

        // Create a MongoDB client
        String connectionString = String.format("mongodb://%s:%s@%s:%d", username, password, host, port);
        try (MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new com.mongodb.ConnectionString(connectionString))
                        .build()
        )) {
            // Get database and collection
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Prepare the document
            Document document = new Document("data", new Document(payload));

            // Insert the document into the collection
            collection.insertOne(document);
            System.out.println("Payload stored successfully in MongoDB: " + document.toJson());
        }
    }


}
