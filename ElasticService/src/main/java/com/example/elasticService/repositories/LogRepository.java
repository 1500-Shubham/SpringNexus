package com.example.elasticService.repositories;

import com.example.elasticService.models.LogEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface LogRepository extends ElasticsearchRepository<LogEntity, String> {
    // Custom query method to find logs by message
    void deleteById(String id);
    void deleteByMessage(String message);
    void deleteByLevel(String level);
    List<LogEntity> findByMessage(String message);
    List<LogEntity> findByLevel(String level);
}
