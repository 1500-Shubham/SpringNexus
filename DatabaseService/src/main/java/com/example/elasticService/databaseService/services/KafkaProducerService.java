package com.example.elasticService.databaseService.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLogToKafka(String logMessage) {
        kafkaTemplate.send("log-topic", logMessage);  // Send to "log-topic"
    }
}
