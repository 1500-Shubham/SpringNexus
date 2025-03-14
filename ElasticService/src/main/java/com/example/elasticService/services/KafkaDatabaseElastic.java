package com.example.elasticService.services;



import com.example.elasticService.models.LogEntity;
import com.example.elasticService.repositories.LogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@EnableKafka
public class KafkaDatabaseElastic {

    @Autowired
    private LogRepository logRepository;

    @KafkaListener(topics = "log-topic", groupId = "log-consumer-group")
    public void consume(String message) {
        try {
//            System.out.println("My message looks like: " + message);
            // Deserialize the Kafka message
            ObjectMapper objectMapper = new ObjectMapper();
            LogEntity log = objectMapper.readValue(message, LogEntity.class);
//            System.out.println("My Log looks like: " + log);
            // Save the log to Elasticsearch
            logRepository.save(log);

            System.out.println("Consumed and stored log: " + log);
        } catch (IOException e) {
            System.out.println("Failed to process message: " + e.getMessage());
        }
    }



}
