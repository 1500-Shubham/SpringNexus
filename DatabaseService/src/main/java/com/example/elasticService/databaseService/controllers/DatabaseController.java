package com.example.elasticService.databaseService.controllers;


import com.example.elasticService.databaseService.services.DatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/storePayload")
    public ResponseEntity<String> storePayload(@RequestParam Integer userId, @RequestBody Map<String, Object> payload) {
        try {
//            System.out.println(payload+"Checking payload");
            databaseService.storePayload(userId, payload);
            return ResponseEntity.ok("Payload stored successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error storing payload: " + e.getMessage());
        }
    }
}
