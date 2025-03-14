package com.example.elasticService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ElasticServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticServiceApplication.class, args);
	}

}
