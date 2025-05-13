//package com.example.elasticService.controllers;
//
//
//import com.example.elasticService.dto.LogDTO;
//import com.example.elasticService.models.LogEntityElasticBean;
//import com.example.elasticService.services.LogServiceElasticBean;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/logs")
//public class LogControllerElasticBean {
//
//    @Value("${elasticsearch.host}")
//    private String host;
//
//    @Value("${elasticsearch.port}")
//    private int port;
//
//    private final LogServiceElasticBean logService;
//
//    public LogControllerElasticBean(LogServiceElasticBean logService) {
//        this.logService = logService;
//    }
//
//    @PostConstruct
//    public void logElasticsearchUrl() {
//        System.out.println("📡 Connecting to Elasticsearch at: http://" + host + ":" + port);
//    }
//
//    @GetMapping
//    public List<LogEntityElasticBean> getAllLogs() throws IOException {
//        return logService.getAllLogs();
//    }
//
//    @PostMapping
//    public LogEntityElasticBean createLog(@RequestBody LogDTO log) throws IOException {
//        return logService.saveLog(log);
//    }
//
//    @GetMapping("/level")
//    public List<LogEntityElasticBean> getLogsByLevel(@RequestParam String level) throws IOException {
//        return logService.findLogsByLevel(level);
//    }
//
//    @GetMapping("/search")
//    public List<LogEntityElasticBean> getLogByMessage(@RequestParam String message) throws IOException {
//        return logService.findLogsByMessage(message);
//    }
//
//    @DeleteMapping("/delete/id")
//    public void deleteLogById(@RequestParam String id) throws IOException {
//        logService.deleteLogById(id);
//    }
//
//    @DeleteMapping("/delete/message")
//    public void deleteLogByMessage(@RequestParam String message) throws IOException {
//        logService.deleteLogByMessage(message);
//    }
//
//    @DeleteMapping("/delete/level")
//    public void deleteLogByLevel(@RequestParam String level) throws IOException {
//        logService.deleteLogByLevel(level);
//    }
//}
