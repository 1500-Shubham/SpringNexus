package com.example.elasticService.controllers;

import com.example.elasticService.dto.LogDTO;
import com.example.elasticService.models.LogEntity;
import com.example.elasticService.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

    // Endpoint to get all logs
    @GetMapping
    public List<LogEntity> getAllLogs() {
        return logService.getAllLogs();
    }

    @PostMapping
    public LogEntity createLog(@RequestBody LogDTO log) {
        System.out.println("Create Log" + log);
        return logService.saveLog(log);
    }

    @GetMapping("/level")
    public List<LogEntity> getLogsByLevel(@RequestParam String level) {
        return logService.findLogsByLevel(level);
    }

    // Endpoint to get logs by message
    @GetMapping("/search")
    public List<LogEntity> getLogByMessage(@RequestParam String message) {
        return logService.findLogsByMessage(message);
    }

    // Endpoint to delete log by ID
    @DeleteMapping("/delete/id/")
    public void deleteLogById(@RequestParam String id) {
        logService.deleteLogById(id);
    }

    // Endpoint to delete log by message
    @DeleteMapping("/delete/message")
    public void deleteLogByMessage(@RequestParam String message) {
        logService.deleteLogByMessage(message);
    }

    // Endpoint to delete log by level
    @DeleteMapping("/delete/level")
    public void deleteLogByLevel(@RequestParam String level) {
        logService.deleteLogByLevel(level);
    }

}
