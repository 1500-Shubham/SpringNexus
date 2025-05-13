package com.example.elasticService.services;



import com.example.elasticService.dto.LogDTO;
//import com.example.elasticService.models.LogEntity;
//import com.example.elasticService.repositories.LogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@EnableKafka
public class KafkaDatabaseElastic {
    private final Map<String, LogDTO> logStore = new ConcurrentHashMap<>();
//    private final LogRepository logRepository;
//    public KafkaDatabaseElastic(LogRepository logRepository) {
//        this.logRepository = logRepository;
//    }

//    private final LogServiceElasticBean logServiceElasticBean;
//    public KafkaDatabaseElastic(LogServiceElasticBean logServiceElasticBean) {
//        this.logServiceElasticBean = logServiceElasticBean;
//    }

    // defined a component bean for these values in KafkaListenerConfig to inject value at compile time
    @KafkaListener(topics = "#{@myKafkaConfig.topicName}", groupId = "#{@myKafkaConfig.kafkaGroupId}")
    public void consume(String message) {
        try {
            // this message is in JSON form {"message":"msg","level":"INFO"};
            System.out.println("My message looks like: " + message);
            // Deserialize the Kafka message
            ObjectMapper objectMapper = new ObjectMapper();
            LogDTO log = objectMapper.readValue(message, LogDTO.class);
            System.out.println("My Log looks like from Kafka: " + log);
            // Save the log to Elasticsearch
//            logRepository.save(log);
            log.setId(UUID.randomUUID().toString());
            storeLog(log);
            System.out.println("Consumed and stored log: " + log);
        } catch (IOException e) {
            System.out.println("Failed to process message: " + e.getMessage());
        }
    }

    public void storeLog(LogDTO log) {
        logStore.put(UUID.randomUUID().toString(), log); // Random key
    }

    public Collection<LogDTO> getAllLogs() {
        return logStore.values();
    }

}
