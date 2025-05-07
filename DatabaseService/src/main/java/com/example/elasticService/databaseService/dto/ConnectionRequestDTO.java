package com.example.elasticService.databaseService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectionRequestDTO {

    private String type;  // This will map to the "type" key
    @JsonProperty("connectionDetails")
    private ConnectionDetailsDTO connectionDetails;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ConnectionDetailsDTO getConnectionDetailsDTO() {
        return connectionDetails;
    }

    public void setConnectionDetailsDTO(ConnectionDetailsDTO connectionDetailsDTO) {
        this.connectionDetails = connectionDetailsDTO;
    }
}
