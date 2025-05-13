package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.models.User;
import com.example.AuthenticationService.repository.UserRepository;
import com.example.AuthenticationService.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/forward")
public class ForwarderController {


    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserRepository repository;

    @Value("${database.service.url}")
    private String databaseServiceURL;
    @Value("${ollama.service.url}")
    private String ollamaServiceURL;

    @Autowired
    private JwtService jwtService;
    @RequestMapping("/{service}/**")
    public ResponseEntity<?> forward( @RequestHeader("Authorization") String authHeader, HttpMethod method, HttpServletRequest request, @RequestBody(required = false) String body) {
        String token = authHeader.replace("Bearer ", "").trim();
        String username = jwtService.extractUsername(token); // custom method you wrote
        Optional<User> user=repository.findByUsername(username);
        Long userId= user.get().getId();
        // 3. Determine target service URL
        String requestURI = request.getRequestURI(); // /forward/dbservice/connections/create
        String forwardPath = requestURI.replaceFirst("/forward/[^/]+", ""); // /connections/create
        String service = request.getRequestURI().split("/")[2]; // dbservice
        // 4. Target base URLs
        Map<String, String> serviceUrls = Map.of(
                "dbservice",databaseServiceURL ,
                "ollamaservice", ollamaServiceURL
        );

        String baseUrl = serviceUrls.get(service);
        if (baseUrl == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Unknown service: " + service);
        }
        String queryString = request.getQueryString();
        String fullUrl = baseUrl + forwardPath + (queryString != null ? "?" + queryString : "");

        // 6. Forward request with user_id header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-ID", String.valueOf(userId));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // 7. Forward request using original HTTP method
        return restTemplate.exchange(fullUrl, method, entity, String.class);
    }
}

