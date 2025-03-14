package com.example.AuthenticationService.service;

import com.example.AuthenticationService.models.User;
import com.example.AuthenticationService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GoogleOAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;  // Your JWT generation service

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Use the OAuth2User to fetch details (email, name, etc.)
    public String handleGoogleOAuth(OAuth2User oauthUser) {
        String email = oauthUser.getAttribute("email");
        String username = oauthUser.getAttribute("name");
        String phone = oauthUser.getAttribute("phone");  // Get phone number from OAuth2 user attributes
        if (phone == null) {
            phone = "000-000-0000";  // Default phone value
        }
        // Check if the user already exists in the database using their email
        Optional<User> userOptEmail = userRepository.findByEmail(email);
        Optional<User> userOptUsername = userRepository.findByUsername(username);
        if (userOptEmail.isPresent()) {
            // User exists, just return a JWT token
            User existingUser = userOptEmail.get();
            return jwtService.generateToken(existingUser);
        }
        else if (userOptUsername.isPresent()) {
            // User exists, just return a JWT token
            User existingUser = userOptUsername.get();
            return jwtService.generateToken(existingUser);
        }
        else {
            // User does not exist, create a new user in the DB
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPhone(phone);  // Default value for phone
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // Generate a default password for OAuth users

            userRepository.saveConnectionManually(user.getEmail(), user.getPassword(),user.getPhone(),user.getUsername());
            // manually query run error handle default wala

            return jwtService.generateToken(user);
        }
    }
}

