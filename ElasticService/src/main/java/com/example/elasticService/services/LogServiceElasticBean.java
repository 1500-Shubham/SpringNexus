//package com.example.elasticService.services;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch.core.*;
//import co.elastic.clients.elasticsearch.core.search.Hit;
//import com.example.elasticService.dto.LogDTO;
//import com.example.elasticService.models.LogEntityElasticBean;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class LogServiceElasticBean {
//
//    private static final String INDEX = "logs";
//    private final ElasticsearchClient client;
//
//    public LogServiceElasticBean(ElasticsearchClient client) {
//        this.client = client;
//    }
//
//    public LogEntityElasticBean saveLog(LogDTO dto) throws IOException {
//        // Using setters to create LogEntityElasticBean
//        LogEntityElasticBean log = new LogEntityElasticBean();
//        log.setMessage(dto.getMessage());
//        log.setLevel(dto.getLevel());
//
//        // Indexing the document
//        IndexResponse response = client.index(i -> i
//                .index(INDEX)
//                .document(log)
//        );
//
//        log.setId(response.id());
//        return log;
//    }
//
//    public List<LogEntityElasticBean> findLogsByLevel(String level) throws IOException {
//        SearchResponse<LogEntityElasticBean> response = client.search(s -> s
//                        .index(INDEX)
//                        .query(q -> q
//                                .match(m -> m
//                                        .field("level")
//                                        .query(level))),
//                LogEntityElasticBean.class);
//
//        return response.hits().hits().stream()
//                .map(Hit::source)
//                .collect(Collectors.toList());
//    }
//
//    public List<LogEntityElasticBean> findLogsByMessage(String message) throws IOException {
//        SearchResponse<LogEntityElasticBean> response = client.search(s -> s
//                        .index(INDEX)
//                        .query(q -> q
//                                .match(m -> m
//                                        .field("message")
//                                        .query(message))),
//                LogEntityElasticBean.class);
//
//        return response.hits().hits().stream()
//                .map(Hit::source)
//                .collect(Collectors.toList());
//    }
//
//    public void deleteLogById(String id) throws IOException {
//        client.delete(DeleteRequest.of(d -> d.index(INDEX).id(id)));
//    }
//
//    public void deleteLogByMessage(String message) throws IOException {
//        client.deleteByQuery(d -> d
//                .index(INDEX)
//                .query(q -> q
//                        .match(m -> m
//                                .field("message")
//                                .query(message))));
//    }
//
//    public void deleteLogByLevel(String level) throws IOException {
//        client.deleteByQuery(d -> d
//                .index(INDEX)
//                .query(q -> q
//                        .match(m -> m
//                                .field("level")
//                                .query(level))));
//    }
//
//    public List<LogEntityElasticBean> getAllLogs() throws IOException {
//        SearchResponse<LogEntityElasticBean> response = client.search(s -> s
//                        .index(INDEX)
//                        .query(q -> q.matchAll(m -> m)),
//                LogEntityElasticBean.class);
//
//        return response.hits().hits().stream()
//                .map(Hit::source)
//                .collect(Collectors.toList());
//    }
//}