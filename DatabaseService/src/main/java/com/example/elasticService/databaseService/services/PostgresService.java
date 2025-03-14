package com.example.elasticService.databaseService.services;
import com.example.elasticService.databaseService.models.SQLiteConnectionEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;

@Service
public class PostgresService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void storePayloadInPostgres(SQLiteConnectionEntity connectionEntity, Map<String, Object> payload) throws Exception {
        // Deserialize connectionString to extract connection details
        Map<String, Object> connectionDetails = objectMapper.readValue(connectionEntity.getConnectionString(), Map.class);
//        System.out.println(connectionDetails+"All connectionsDEtails of user");
        String host = (String) connectionDetails.get("host");
        int port = (int) connectionDetails.get("port");
        String username = (String) connectionDetails.get("username");
        String password = (String) connectionDetails.get("password");
        String databaseName = "payload_db"; // Can be replaced dynamically
        String tableName = "movie";       // Can be replaced dynamically

        // Create a DataSource for the default database (postgres)
        DataSource adminDataSource = createDataSource(host, port, username, password, "postgres");

        // Ensure the database exists
        createDatabaseIfNotExists(adminDataSource, databaseName);

        // Create a DataSource for the target database
        DataSource targetDataSource = createDataSource(host, port, username, password, databaseName);

        // Ensure the table exists
        createTableIfNotExists(targetDataSource, tableName,payload);

        // Store payload in the table
        storeDataInTable(targetDataSource, tableName, payload);
    }

    private DataSource createDataSource(String host, int port, String username, String password, String databaseName) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{host});
        dataSource.setPortNumbers(new int[]{port});
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setDatabaseName(databaseName);
        return dataSource;
    }

    private void createDatabaseIfNotExists(DataSource dataSource, String databaseName) throws SQLException {
        String sql = "SELECT 1 FROM pg_database WHERE datname = ?";
        String createDatabaseSql = "CREATE DATABASE " + databaseName;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, databaseName);

            try (var resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    try (Statement createDbStatement = connection.createStatement()) {
                        createDbStatement.executeUpdate(createDatabaseSql);
                        System.out.println("Database created: " + databaseName);
                    }
                } else {
                    System.out.println("Database already exists: " + databaseName);
                }
            }
        }
    }

    // created Table and Column according to keys in Payload
    private void createTableIfNotExists(DataSource dataSource, String tableName, Map<String, Object> payload) throws SQLException {
        // Check if the table exists
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet resultSet = metaData.getTables(null, null, tableName, null)) {
            if (!resultSet.next()) {
                // If the table doesn't exist, create it
                StringBuilder createTableSQL = new StringBuilder("CREATE TABLE " + tableName + " (");
                for (String key : payload.keySet()) {
                    if (createTableSQL.length() > ("CREATE TABLE " + tableName + " (").length()) {
                        createTableSQL.append(", ");
                    }
                    createTableSQL.append(key).append(" TEXT"); // Defaulting to TEXT type
                }
                createTableSQL.append(")");

                try (Statement createTableStatement = connection.createStatement()) {
                    createTableStatement.executeUpdate(createTableSQL.toString());
                    System.out.println("Table " + tableName + " created successfully." + "Query" +createTableSQL.toString() );
                }
            }
        }
    }

    private void storeDataInTable(DataSource dataSource, String tableName, Map<String, Object> payload) throws SQLException {
        System.out.println(payload+"Final payload to store in "+tableName);
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (String key : payload.keySet()) {
            if (columns.length() > 0) {
                columns.append(", ");
                placeholders.append(", ");
            }
            columns.append(key);
            placeholders.append("?");
        }

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int index = 1;
            for (String key : payload.keySet()) {
                statement.setObject(index++, payload.get(key));
            }

            statement.executeUpdate();
            System.out.println("Data inserted successfully into table: " + tableName);
        }
    }
}
