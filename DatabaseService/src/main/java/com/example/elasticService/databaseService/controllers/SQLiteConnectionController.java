package com.example.elasticService.databaseService.controllers;

import com.example.elasticService.databaseService.dto.ConnectionDetailsDTO;
import com.example.elasticService.databaseService.dto.ConnectionRequestDTO;
import com.example.elasticService.databaseService.models.SQLiteConnectionEntity;
import com.example.elasticService.databaseService.services.SQLiteConnectionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/connections")
public class SQLiteConnectionController {

    private final SQLiteConnectionService SQLiteConnectionService;

    public SQLiteConnectionController(SQLiteConnectionService SQLiteConnectionService) {
        this.SQLiteConnectionService = SQLiteConnectionService;
    }

    // Create a new connection (requires userId, connection type, and connection details)
    @PostMapping("/create")
    public  List<SQLiteConnectionEntity> createConnection(HttpServletRequest request, @RequestBody ConnectionRequestDTO connectionRequestDTO) throws Exception {
        // Extract type and connection details from the request
        Integer userId= Integer.valueOf(request.getHeader("X-User-ID"));
        String type = connectionRequestDTO.getType();
        ConnectionDetailsDTO connectionDetailsDTO = connectionRequestDTO.getConnectionDetailsDTO();
//        System.out.println("connectionDetailsDTO" + connectionDetailsDTO);
        return SQLiteConnectionService.createConnection(userId, type, connectionDetailsDTO);
    }

    // Delete a connection by userId and connection ID
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteConnection(HttpServletRequest request, @RequestParam Long connectionId) {
        try {
            Integer userId= Integer.valueOf(request.getHeader("X-User-ID"));
            SQLiteConnectionService.deleteConnection(userId, connectionId);
            return ResponseEntity.ok("Connection deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection not found.");
        }
    }

    // Fetch all connections by userId
    @GetMapping("/fetch")
    public List<SQLiteConnectionEntity> fetchAllConnections(HttpServletRequest request) {
        Integer userId= Integer.valueOf(request.getHeader("X-User-ID"));
        return SQLiteConnectionService.fetchAllConnections(userId);
    }

    // Logout and clear user_id data
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        try {
            Integer userId= Integer.valueOf(request.getHeader("X-User-ID"));
            SQLiteConnectionService.logout(userId);
            return ResponseEntity.ok("Local map Connection deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection not found.");
        }

    }
}

