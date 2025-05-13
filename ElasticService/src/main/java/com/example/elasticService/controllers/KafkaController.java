package com.example.elasticService.controllers;

import com.example.elasticService.dto.LogDTO;
//import com.example.elasticService.models.LogEntity;
import com.example.elasticService.services.KafkaDatabaseElastic;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/kafkaLogs")
public class KafkaController {

    private final KafkaDatabaseElastic KafkaDatabaseElastic;

    public KafkaController(KafkaDatabaseElastic KafkaDatabaseElastic) {
        this.KafkaDatabaseElastic = KafkaDatabaseElastic;
    }

    // Endpoint to get all logs
    @GetMapping
    public Collection<LogDTO> getAllLogs() {
        return KafkaDatabaseElastic.getAllLogs();
    }

}
