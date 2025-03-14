package com.example.elasticService.databaseService.models;

public class SQLiteConnectionRequestHelper {

    private String type;  // This will map to the "type" key
    private SQLiteConnectionDetailsHelper SQLiteConnectionDetailsHelper;  // This will map to the "connectionDetails" object

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SQLiteConnectionDetailsHelper getConnectionDetails() {
        return SQLiteConnectionDetailsHelper;
    }

    public void setConnectionDetails(SQLiteConnectionDetailsHelper SQLiteConnectionDetailsHelper) {
        this.SQLiteConnectionDetailsHelper = SQLiteConnectionDetailsHelper;
    }

    @Override
    public String toString() {
        return "ConnectionRequest{" +
                "type='" + type + '\'' +
                ", connectionDetails=" + SQLiteConnectionDetailsHelper +
                '}';
    }
}
