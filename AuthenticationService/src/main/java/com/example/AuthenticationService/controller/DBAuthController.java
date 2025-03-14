package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.ErrorHandling.BadRequestException;
import com.example.AuthenticationService.models.User;
import com.example.AuthenticationService.service.DBAuthenticationService;
import com.example.AuthenticationService.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/db")
public class DBAuthController {

    private final DBAuthenticationService DBAuthenticationService;
    private final JwtService jwtService;

    public DBAuthController(DBAuthenticationService DBAuthenticationService, JwtService jwtService) {
        this.DBAuthenticationService = DBAuthenticationService;
        this.jwtService = jwtService;
    }

    // Public endpoint: Register a user
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
//        System.out.println("User Body "+  user.toString());
        String token = DBAuthenticationService.register(user); // Get the JWT token directly
        return ResponseEntity.ok(token); // Return the token as a String
    }

    // Public endpoint: Authenticate and return JWT token
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String usernameOrEmail = request.get("usernameOrEmail");
        String password = request.get("password");

        if (usernameOrEmail == null || password == null) {
            throw new BadRequestException("Username/Email and Password are required");
        }

        String token = DBAuthenticationService.authenticate(usernameOrEmail, password);
        return ResponseEntity.ok(token);
    }

    // Protected endpoint: Only accessible with valid JWT
    @GetMapping("/test")
    public ResponseEntity<String> testJwtAuthentication(@RequestHeader("Authorization") String authHeader) {
        // Extract token from "Authorization: Bearer <token>"
        String username = DBAuthenticationService.test(authHeader);
        return ResponseEntity.ok("Hello, " + username + "! Your token is valid.");
    }
}

