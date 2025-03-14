# Docker

### Basics

- **docker ps**
- **docker ps -a**
- **docker stop <container_name_or_id>**
- **docker rm <container_name_or_id>**
- **docker rm -f elasticsearch-container**
- **docker images**
- **docker rmi abc12345xyz** // remove image

### Network Commands
- **docker network create elastic** // Create a network for Elastic and Kibana to work together  
- **docker network ls** // List all networks  
- **docker network create <network_name>**  
- **docker network inspect <network_name>**  
- **docker network rm <network_name>**  
- **docker network prune** // Delete unused networks  

### Docker Compose
- **docker-compose -f Kafka.yml -p kafka-zookeper up -d** // `-p` indicates the name of the running container  

### Dockerfile
- **docker build -t custom-elasticsearch .**
- If Dockerfile has a custom name:  
  **docker build -f MyDockerfile -t custom-elasticsearch .**

# Kafka 
- **docker exec -it kafka-container kafka-topics --create --topic my-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1**

## Kafka Basics

### Setup Kafka
- **docker-compose -f Kafka.yml -p kafka-zooker-zookeper up -d**

### Kafka Commands
- **Create a Topic**:  
**docker exec -it kafka-zooker kafka-topics --create --topic my-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1**
- **List Topics**:  
**docker exec -it kafka-zooker kafka-topics --list --bootstrap-server localhost:9092**
- **Produce Messages**:  
**docker exec -it kafka-zooker kafka-console-producer --topic my-topic --bootstrap-server localhost:9092**
- **Consume Messages**:  
**docker exec -it kafka-zooker kafka-console-consumer --topic my-topic --bootstrap-server localhost:9092 --from-beginning**

---



# ElasticSearch
- **docker run --name elasticsearch-container -p 9200:9200 -p 9300:9300 custom-elasticsearch**  
- Run with network:  
  **docker run --name elasticsearch-container --net elastic -p 9200:9200 -p 9300:9300 custom-elasticsearch**

---

# Kibana

### Direct Run for Checking ElasticSearch
- **docker run --name kibana --net elastic -p 5601:5601 -e "ELASTICSEARCH_HOSTS=http://elasticsearch-container:9200" -d docker.elastic.co/kibana/kibana:8.5.0**

### Kibana and Elastic Enrollment Token
- **docker exec -it elasticsearch-container /bin/bash**  
  **bin/elasticsearch-create-enrollment-token -s kibana**

### Disable Security (if needed for local use)
- Add the following to `elasticsearch.yml` or the Dockerfile:
-ENV discovery.type=single-node 
- ENV xpack.security.enabled=false 
- ENV xpack.security.enrollment.enabled=false

- For Kibana, add:  
**-e "xpack.security.enabled=false"**

---

# SQLLite
- sqlite3 --version
- sqlite3 database_service.db
- touch database_service.db

# MongoDb
- mongosh
- use payload_db | admin
- db.createCollection("movies")
- db.movies.insertOne({ id_: ObjectId(), data: { movie: "heraPheri", rating: "10" } })
- db.movies.find().pretty()
- db.movies.find({ "data.movie": "heraPheri" })
- db.movies.updateOne(
    { "data.movie": "heraPheri" },                // Filter
    { $set: { "data.rating": "9" } }             // Update
)
- db.movies.updateMany(
    { "data.rating": "10" },                     // Filter
    { $set: { "data.rating": "8" } }             // Update
)
- db.movies.deleteOne({ "data.movie": "heraPheri" })
- db.movies.deleteMany({ "data.rating": "10" })
- db.movies.drop() //delete collection
- db.dropDatabase() // database remove

# Redis
- redis-cli -a yourpassword
- SET user:1 "JohnDoe"
- GET user:1 
- DEL user:1
- EXISTS user:1
- KEYS *