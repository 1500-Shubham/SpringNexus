## ElasticService
- This is consumer of Kafka (need Kafka server running beforehand)
- POST message from Kafka to Elastic
- Steps:
    - Kafka & Elastic configuration in application properties
    - kafkaListenerContainerFactory KafkaConfig
    - Elastic Data format -> Model define and extends ElasticsearchRepository (JPA modelling table -> Functions)
    - KafkaListener any method(topics = "log-topic", groupId = "log-consumer-group") receive message -> logRepo.save(message)
    - Kafka.yml and Elastic Dockerfile to build images and expose their endpoint
- Set Up Kibana image for port 5601 for elastic data to observe
- Using Elastic
    - Method-1 Can directly use ElasticRepository extending your LogRepo
    - Method-2 Define Bean and create ElasticClient and then perform CRUD

## DatabaseService
- Postgres.yml | Redis.yml | Mongodb.yml host port username password
- Works on userID which is forwarded from AuthenticationService after verifying token

    ### Database Controller & Service
    - Based on SQLLite connections -> UserId all connections store same Payload data
    - Type = "Postgres" "MongoDB" "Redis" 
    - Postgres Service -> PGSimpleDataSource for dataSource creation
    - MongoDB  Service -> MongoClient
    - Redis Service -> JedisPool

    ### SQLLiteConnection Controller & Service
    - CRUD on local SQLite DB connections info store (type, connectionDetails(host,port,password))
    - Used JPARepository for CRUD function 
    - Models - SQLite Payload basis created | SQLliteConnectionEntity(connId , UserId, connections)

    ### Kafka Controller & Service
    - Producer of message from this service
    - **KafkaProducerConfigSetup** producerFactory
    - kafkaTemplate in Service to sendMessage(topic)
    - Use the following steps:
        - **Service**: API  
        - **KafkaProducerConfigSetup**  
        - **KafkaTemplate.send** in KafkaService  
        - **ApplicationProperties**


## OllamaService
- First activate Ollama Server at your system 
- API -> http://localhost:11434/api/generate"
- Service | RestTemplate use to create HTTP POST Request with body {model, prompt, stream:false}
- Get response and return the value


## AuthenticationService 
- token create from various methods, then this token will be to create user_id and pass to other services inside Request of API
    -   Created a forwarder controller inside which all endpoints will be send (capturing: request, method, body)- Since this is jwt filter passed, extract user_id for this token
    - Create HttpEntity freshly and breakdown url according to other service endpoint
    - Setting user_id inside headers and pass to other services
- DBAuth | Oauth |TwilioOTP | AuthenticatorOTP&QRCode -> Store these users into DB

### DBAuth
- Some endpoints like login/register permit all -> token generate from DB data and return JWT Service methods
- For other endpoint Bearer:token is passed -> .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
- Internally this filter takes token validates and set in Security Context which can be used by authentication Mananger SecurityContextHolder.getContext().setAuthentication(authToken) // Also UsernamePasswordAuthenticationToken saved
- JWTFilter just check whether token username and password exist in DB
- Whenever authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), password));
then this manager uses UserDetails to fetch username and password and it know password is encoded by Brcypt defined as a bean in security config: So matching username and password from DB UserDetails loadByUsername method


### OAuth
    / http://localhost:8080/oauth2/authorization/google
    -Spring automatically use this url to direct user to oauth window
    -Then user is redirected back to url provided in backend controller
    - Google Cloud Platform Console.
    - spring.properties define
    - Security config add OauthLogin
    - Controller OAuth2AuthenticationToken toh woh endpoint spring understand and return
    - OAuth2User email username from token store inside DB

#### Flow->
##### Method1- When using only oauth as authentication 
- a) Security Config add .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth/loginOrRegister", true)  // After OAuth2 login, redirect to /oauth/loginOrRegister
                        .failureUrl("/oauth/loginFailure") 
                )
- b) Now any non permitted endpoint hit toh by default spring use oauth -> internally calls http://localhost:8080/oauth2/authorization/google -> signin hone ke baad redirect to defaultSuccessUrl

##### Method2- Using JWT for all endpoint and manually triggering Oauth for example: Sign in with google
- a) Spring security define
    - i) .addFilterBefore(jwtAuthenticationFilter,
    - ii)  .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth/loginOrRegister", true)  // After OAuth2 login, redirect to /oauth/loginOrRegister
                        .failureUrl("/oauth/loginFailure") 
                ) // since telling springboot whenever  http://localhost:8080/oauth2/authorization/google is called then go to oauth 
- b)  Controller 
    - i) OAuth2AuthenticationToken oauthToken -> null then redirect manually to oauth2/authorization/google and since you have setup defaultSuccessUrl to the same controller
    - ii) Not null next time fetch details OAuth2User from token

### Twilio OTP Based
- Just have controller and service, dont have credential to test with registered phone number

### Authenticator OTP Based
- TOTP = Time-based One-Time Password How otp generated in authenticator is matched with totp which is constant and generated once. It is generated using: 
    - A shared secret (base32 string) — the totp_secret
    - The current time
- Code : Inside User Table use fields is_first_login and totp_secret, 
    - Whenever first_login is true then give one QR code attaching TOTP GoogleAuthenticatorKey key, Qr = GoogleAuthenticatorQRGenerator.getOtpAuthURL()
    - If first_login is false then simply match OTP from authenticator GoogleAuthenticator gAuth.authorize(user.getTotpSecret(),dto.getOtp()) // totp secret -> CurrentTime -> OTP matches

## Nginx
- Created a nginx.yml file to have nginx image and provided nginx config which has location to server mapping for forwarding.
- Mounted nginx.conf local to container location 

## Docker Containerisation Deployment
- DockerFile of BackendApplication
    - To optimize the Dockerfile, you only need to copy:
    The Maven wrapper (mvnw and .mvn) for building.
    The pom.xml to resolve dependencies.
    The src/ directory for source code.

- image name to Dockerfile
    - docker build --build-arg PROFILE=prod -t authentication_service:prod .
    - docker build --build-arg PROFILE=prod -t database_service:prod .
    - docker build --build-arg PROFILE=prod -t ollama_service:prod .
    - docker build --build-arg PROFILE=prod -t elastic_service:prod .

- Separetly run yml of this service
docker-compose.yml and .env inside it 

- Combine images of everything
docker compose --env-file .env up -d

- Compose up normal yml files
docker-compose -f Kafka-LocalListener.yml -p kafka_zookeper-local up -d

- SAME NETWORK 
    - For one container to understand other container name and ip make sure both are on same NETWORK
    EX: authentication_service env DATABASE_SERVICE_URL=http://database-service:8081 will only work

    - ALSO MAKE SURE
    Root Cause: Separate docker-compose files → Separate Networks
    Even though both services declare spring-nexus as their network in their own docker-compose files, 
    Docker creates separate networks with similar names scoped to each compose project, 
    unless you explicitly share the same external network.
    - METHOD-1 ALL Container in same yml file and same network
    - METHOD-2 Diffenent container on different yml and external network
    docker network create spring-nexus
    ` networks:
        spring-nexus:
        external: true `

## Kubernetes K8s Deployment
- DockerHub - Images of backend build locally can be send here
    - Tag your image - docker tag authentication_service:dev shubham123/authentication_service:dev
    - Login to DockerHub - docker login
    - Push to DockerHub - docker push shubham123/authentication_service:dev
    - Refer inside yml- image: shubham470/springnexus:authentication_service-prod

- Minikube- Local Single Node K8 Cluster
    - brew install minikube
    - minikube start --driver=docker //This spins up a local K8s cluster inside a Docker container
    - kubectl get nodes // Confirming Setup
    - minikube ip http://<minikube-ip>:30083 //access it in your browser or API tool via:
        - But if minikube running in docker container get service url using:
        - `minikube service auth-service-nodeport -n spring-nexus --url`
    - Since SQLLite DB need to be present inside minikube container we can mount it
        - First minikube start -> mount -> deployment and svc up
        - `minikube mount /Users/shubhamkeshari/Documents/VSCode/SpringNexus/KubernetesDeploymentK8s/VolumeMounting:/mnt/springnexus/volumeMounting` And then set the PVC's volume to use a hostPath: since hostPath is localStorage now
         - Now All yaml file can directly access this sqllite db like `path: /mnt/springnexus/database_service.db` 
         - If you delete the Minikube VM/container, all Kubernetes state (including PVCs using hostPath) will be lost : : But is that path is mounted to your system then those changes will be saved inside your computer
        
        
- Namespaces //virtualCluster within K8 cluter
    - kubectl apply -f namespace.yaml
    - kubectl get ns

- ConfigMap- Instead of env use this to declare these variables inside yml Can use inside container environment variables not inside PVC or PV(Resource related)
    - kubectl get configmap
    - kubectl apply -f configmap.yaml
    - kubectl get configmap -n spring-nexus
    - kubectl describe configmap springnexus-config -n spring-nexus
    - Your environment variables into a Kubernetes ConfigMap, and also use `service names as internal DNS` references to allow services to talk to each other within the cluster.

- Secret- 
    - kubectl get secrets
    - kubectl apply -f secret.yaml

- PVC and PV
    - First create PV associated with a volume (can be hostPath) then a claim PVC is done on this PV
    - Mount the PVC in Your Deployment (So in Kubernetes mouting of volume is done using PVC)
    - Process PV -> PVC create then inside Deployment -> Volumes(use PVC) -> volume Mounts(Like Docker that container path to outside Volume)
    - kubectl get pv
    - kubectl get pvc -n spring-nexus
    - kubectl describe pv sqlite-pv -n spring-nexus
    - kubectl describe pvc sqlite-pvc -n spring-nexus
    - kubectl delete pvc sqlite-pvc -n spring-nexus
    - Using Storage Class
      - Provisioners Cloud ex.AWS EBS StorageClass kind: StorageClass
      - StorageClass -> PVC -> PV

- Deployment, PODS and Service
    - [Deployment]
        └─ matchLabels: app=auth-service
            ↓
        [Creates Pods with labels: app=auth-service, component=auth-service-app]
            ↑
        [Service with selector: app=auth-service]
        └─ routes traffic to those Pods

- Commands- 
    - kubectl get pods
    - kubectl describe pods auth-service-deployment-7fbb65f78-tgwqf -n spring-nexus
    - kubectl get svc
    - kubectl apply -f authentication-service-deployment.yml
    - kubectl logs pod-name
    - kubectl logs -f pod-name //stream logs
    - kubectl delete all --all -n spring-nexus
    - kubectl delete configmap | pvc | pv --all -n 
    - kubectl delete service <service-name> -n <namespace>
    - kubectl scale deployment <deployment-name> --replicas=0 -n <namespace>
    - kubectl exec -n spring-nexus -it pod/ollama-deployment-86cdc8d876-5gpwk -- bash
    - kubectl rollout restart deployment <deployment-name> -n <namespace>

    
- K8s Points to Note-
    - Change the OLLAMA_SERVER_URL to explicitly use port 11434: -> http://ollama-service:11434/api/generate {if not port mentioned then listen to 80 port like http://ollama-service:80}


