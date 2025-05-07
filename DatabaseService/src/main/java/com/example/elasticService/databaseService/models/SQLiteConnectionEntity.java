package com.example.elasticService.databaseService.models;

import com.example.elasticService.databaseService.dto.ConnectionDetailsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

@Entity
@Table(name = "connection_table")
public class SQLiteConnectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long connId;

    private Integer userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String connectionString; // JSON as String

    // Default no-argument constructor (required by JPA)
    public SQLiteConnectionEntity() {
    }

    public SQLiteConnectionEntity(Integer userId, String type, ConnectionDetailsDTO connectionDetailsDTO) throws JsonProcessingException {
        this.userId = userId;
        this.type = type;
        // Convert ConnectionDetails object to JSON String
        ObjectMapper objectMapper = new ObjectMapper();
        this.connectionString = objectMapper.writeValueAsString(connectionDetailsDTO);
    }

    // Getters and Setters


    public Long getConnId() {
        return connId;
    }

    public void setConnId(Long connId) {
        this.connId = connId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public String toString() {
        return "ConnectionEntity{" +
                "connId=" + connId +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", connectionString='" + connectionString + '\'' +
                '}';
    }
}
