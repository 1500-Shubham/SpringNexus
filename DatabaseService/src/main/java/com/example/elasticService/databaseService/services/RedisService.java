package com.example.elasticService.databaseService.services;

import com.example.elasticService.databaseService.models.SQLiteConnectionEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

@Service
public class RedisService {

    // Method to create Jedis Pool dynamically using connectionDetails
    private JedisPool createJedisPool(Map<String, Object> connectionDetails) {
        String host = (String) connectionDetails.get("host");
        int port = (int) connectionDetails.get("port");
        String password = (String) connectionDetails.get("password");

        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(10);
//        poolConfig.setMaxIdle(5);
//        poolConfig.setMinIdle(1);
//        poolConfig.setMaxWaitMillis(2000);

        // Create Jedis Pool dynamically with host, port and password from connectionDetails
        return new JedisPool(poolConfig, host, port, 2000, password);
    }
    private final ObjectMapper objectMapper = new ObjectMapper();
    // Method to store the payload in Redis
    public void storePayloadInRedis(SQLiteConnectionEntity connection, String userId, Map<String, Object> payload) throws JsonProcessingException {
        // Fetch the connection string for Redis
        Map<String, Object> connectionDetails = objectMapper.readValue(connection.getConnectionString(), Map.class);
        Long connId= connection.getConnId();
        JedisPool jedisPool = createJedisPool(connectionDetails);

        try (Jedis jedis = jedisPool.getResource()) {
            // Create a Redis key as "userId:connId"
            String redisKey = "User"+userId + ":" + "Conn"+connId;

            // Store the payload (converted to String or JSON) in Redis
            jedis.set(redisKey, payload.toString());  // For JSON payload, use a library like Jackson/Gson to convert it

            System.out.println("Stored data in Redis: " + redisKey + " => " + payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve data from Redis
    public String getDataFromRedis(Map<String, Object> connectionDetails, String userId, String connId) {
        JedisPool jedisPool = createJedisPool(connectionDetails);

        try (Jedis jedis = jedisPool.getResource()) {
            // Create a Redis key as "userId:connId"
            String redisKey = userId + ":" + connId;
            // Get the value stored for the given key
            return jedis.get(redisKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to delete data from Redis
    public void deleteDataFromRedis(Map<String, Object> connectionDetails, String userId, String connId) {
        JedisPool jedisPool = createJedisPool(connectionDetails);

        try (Jedis jedis = jedisPool.getResource()) {
            // Create a Redis key as "userId:connId"
            String redisKey = userId + ":" + connId;
            // Delete the key-value pair
            jedis.del(redisKey);

            System.out.println("Deleted data from Redis: " + redisKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
