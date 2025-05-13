package com.example.elasticService.databaseService.controllers;


import com.example.elasticService.databaseService.models.SQLiteConnectionEntity;
import com.example.elasticService.databaseService.services.DatabaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/storePayload")
    public ResponseEntity<String> storePayload(HttpServletRequest request, @RequestBody Map<String, Object> payload) {
        try {
//            System.out.println(payload+"Checking payload");
            Integer userId= Integer.valueOf(request.getHeader("X-User-ID"));
            databaseService.storePayload(userId, payload);
            return ResponseEntity.ok("Payload stored successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error storing payload: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testing(HttpServletRequest request) {
        String uid = request.getHeader("X-User-ID");
        Integer userId= Integer.valueOf(uid);
        System.out.println("User ID from header: " + userId);
        return ResponseEntity.ok(Map.of("userId", userId, "test", "test"));
    }


}
