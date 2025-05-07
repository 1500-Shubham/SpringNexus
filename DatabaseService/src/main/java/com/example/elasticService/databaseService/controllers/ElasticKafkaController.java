package com.example.elasticService.databaseService.controllers;

import com.example.elasticService.databaseService.dto.LogDTO;
import com.example.elasticService.databaseService.services.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class ElasticKafkaController {

    private final KafkaProducerService kafkaProducerService;

    public ElasticKafkaController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping
    public ResponseEntity<String> postLog(@RequestBody LogDTO dto) {
        // directly sending via API to kafka
        String logMessage = String.format("{\"message\": \"%s\", \"level\": \"%s\"}",
                dto.getMessage(), dto.getLevel());
        System.out.println("DatabaseService"+logMessage);
        kafkaProducerService.sendLogToKafka(logMessage);
        return ResponseEntity.ok("Log sent to Kafka");
    }
}
