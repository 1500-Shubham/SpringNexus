package com.example.elasticService.services;

import com.example.elasticService.dto.LogDTO;
import com.example.elasticService.models.LogEntity;
import com.example.elasticService.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    // Save log
    public LogEntity saveLog(LogDTO log) {
        LogEntity logEntity=new LogEntity();
        logEntity.setMessage(log.getMessage());
        logEntity.setLevel(log.getLevel());
        return logRepository.save(logEntity);
    }

    // Method to retrieve all logs
    public List<LogEntity> getAllLogs() {
        Page<LogEntity> page = (Page<LogEntity>) logRepository.findAll();
        return page.getContent();  // Convert Page to List
    }

    public void deleteLogById(String id) {
        logRepository.deleteById(id);
    }

    // Delete log by message
    public void deleteLogByMessage(String message) {
        logRepository.deleteByMessage(message);
    }

    // Delete log by level
    public void deleteLogByLevel(String level) {
        logRepository.deleteByLevel(level);
    }

    // Optionally, you can add methods to retrieve logs before deletion if needed
    public List<LogEntity> findLogsByMessage(String message) {
        return logRepository.findByMessage(message);
    }

    public List<LogEntity> findLogsByLevel(String level) {
        return logRepository.findByLevel(level);
    }

}
