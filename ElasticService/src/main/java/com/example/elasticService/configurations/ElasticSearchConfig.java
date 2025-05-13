//package com.example.elasticService.configurations;
//
//
//import org.apache.hc.client5.http.ssl.TrustAllStrategy;
//import org.apache.hc.core5.ssl.SSLContextBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//import javax.net.ssl.SSLContext;
//
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.example.elasticService.repositories")
//public class ElasticSearchConfig extends ElasticsearchConfiguration {
//
//    @Value("${spring.data.elasticsearch.username}")
//    private String username;
//
//    @Value("${spring.data.elasticsearch.password}")
//    private String password;
//
//    @Override
//    public ClientConfiguration clientConfiguration() {
//        return ClientConfiguration.builder()
//                .connectedTo("localhost:9200")
//                .usingSsl(builSSLContext())
//                .withBasicAuth(username, password)
//                .build();
//    }
//
//    private static SSLContext builSSLContext() {
//        try {
//            return new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
