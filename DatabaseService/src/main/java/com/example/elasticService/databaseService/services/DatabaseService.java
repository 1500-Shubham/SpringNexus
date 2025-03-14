package com.example.elasticService.databaseService.services;

import com.example.elasticService.databaseService.models.SQLiteConnectionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {
    private final SQLiteConnectionService connectionService;
    private final PostgresService postgresService;
    private final MongoDBService mongoDBService;
    private final RedisService redisService;

    public DatabaseService(SQLiteConnectionService connectionService, PostgresService postgresService,MongoDBService mongoDBService,RedisService redisService) {
        this.connectionService = connectionService;
        this.postgresService = postgresService;
        this.mongoDBService=mongoDBService;
        this.redisService=redisService;
    }

    public void storePayload(Integer userId, Map<String, Object> payload) throws Exception {
        // Fetch all connections for the user
        List<SQLiteConnectionEntity> connections = connectionService.fetchAllConnections(userId);
//        System.out.println(connections+"All connections of user");
        // Iterate through connections and handle Postgres connections
        for (SQLiteConnectionEntity connection : connections) {
            if ("postgres".equalsIgnoreCase(connection.getType())) {
                postgresService.storePayloadInPostgres(connection, payload);
            }
            else if ("mongodb".equalsIgnoreCase(connection.getType())) {
                // Handle MongoDB connections
                mongoDBService.storePayloadInMongoDB(connection, payload);
            }
            // Handle Redis connection
            else if ("redis".equalsIgnoreCase(connection.getType())) {
                  // Store the payload in Redis (using userId and connId as key)
                redisService.storePayloadInRedis(connection, String.valueOf(userId), payload);
            }
        }
    }
}
