//package com.example.elasticService.models;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//import java.util.UUID;
//
//
////  This was used with LogRepository
//@Document(indexName = "logs")
//public class LogEntity {
//
//
//        @Id
//        private String id = UUID.randomUUID().toString();  // Auto-generate ID using UUID
//        private String message;
//        private String level;
//
//        public String getMessage() {
//                return message;
//        }
//
//        public void setMessage(String message) {
//                this.message = message;
//        }
//
//        public String getId() {
//                return id;
//        }
//
//        public void setId(String id) {
//                this.id = id;
//        }
//
//        public String getLevel() {
//                return level;
//        }
//
//        public void setLevel(String level) {
//                this.level = level;
//        }
//
//        @Override
//        public String toString() {
//                return "LogEntity{" +
//                        "id='" + id + '\'' +
//                        ", message='" + message + '\'' +
//                        ", level='" + level + '\'' +
//                        '}';
//        }
//}
