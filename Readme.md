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
- token create | verify | forwarder userID to other service
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