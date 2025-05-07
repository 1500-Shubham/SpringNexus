package com.example.elasticService.databaseService.services;

import com.example.elasticService.databaseService.dto.ConnectionDetailsDTO;
import com.example.elasticService.databaseService.models.SQLiteConnectionEntity;
import com.example.elasticService.databaseService.repositories.SQLConnectionRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SQLiteConnectionService {

    private final Map<Integer, List<SQLiteConnectionEntity>> userConnectionMap = new HashMap<>();  // Change map to use userId

    private final SQLConnectionRepository SQLConnectionRepository;

    public SQLiteConnectionService(SQLConnectionRepository SQLConnectionRepository) {
        this.SQLConnectionRepository = SQLConnectionRepository;
    }

    // Create a connection by user_id
    public  List<SQLiteConnectionEntity> createConnection(Integer userId, String type, ConnectionDetailsDTO connectionDetailsDTO) throws Exception {
        // Create the Connection object
        SQLiteConnectionEntity newConnection = new SQLiteConnectionEntity(userId, type, connectionDetailsDTO);

//        System.out.println("final coonection connectionDetailsDTO " + connectionDetailsDTO);

        // SQLITE SAVE METHOD PROBLEM
        // insert into connection_table (connection_string,type,user_id,conn_id) values (?,?,?,default)
        // this is one such problem in sqllite autoIncrement mein jpa default send karta aur issue
        //connectionRepository.save(newConnection); default save method of jap

        // created custom query
       SQLConnectionRepository.saveConnectionManually(newConnection.getConnectionString(), type, userId);

        // Store in the in-memory map (userId -> List of Connections)
        userConnectionMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(newConnection);

        return  userConnectionMap.get(userId);
    }

    // Delete Connection by user_id and connection ID
    public void deleteConnection(Integer userId, Long connectionId) {
        // Remove from SQLite
//        System.out.println(userId+"userid"+connectionId);
        SQLConnectionRepository.deleteById(connectionId);

        // Remove from in-memory map
        List<SQLiteConnectionEntity> connections = userConnectionMap.get(userId);
        if (connections != null) {
            connections.removeIf(c -> c.getConnId().equals(connectionId));
        }
    }

    // Fetch all Connections for a given user_id and store them locally
    public List<SQLiteConnectionEntity> fetchAllConnections(Integer userId) {

//        List<ConnectionEntity> connections = userConnectionMap.getOrDefault(userId, Collections.emptyList());
        // Fetch from database and update map if needed
        List<SQLiteConnectionEntity> connections = SQLConnectionRepository.findByUserId(userId);
        userConnectionMap.put(userId, connections);
        // fetch map and database updated

        return connections;
    }

    // Logout -> Remove user_id from in-memory map
    public void logout(Integer userId) {
        userConnectionMap.remove(userId);
    }
}

