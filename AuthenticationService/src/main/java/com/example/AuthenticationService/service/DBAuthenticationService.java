package com.example.AuthenticationService.service;

import com.example.AuthenticationService.ErrorHandling.BadRequestException;
import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.models.User;
import com.example.AuthenticationService.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DBAuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public DBAuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
                                   AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // Register a new user and return the JWT token
    public String register(UserDTO request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        System.out.println(user.getPassword()+"User Hashed Password");
        user.setUsername(request.getUsername());

        // SQLITE SAVE METHOD PROBLEM
        // insert into connection_table (connection_string,type,user_id,conn_id) values (?,?,?,default)
        // this is one such problem in sqllite autoIncrement mein jpa default send karta aur issue
        //connectionRepository.save(newConnection); default save method of jap

        repository.saveConnectionManually(user.getEmail(), user.getPassword(),user.getPhone(),user.getUsername());
        return jwtService.generateToken(user); // Return only the token as a String
    }

    // Authenticate a user and return the JWT token
    public String authenticate(String usernameOrEmail, String password) {
        try {
            User user = repository.findByUsername(usernameOrEmail)
                    .or(() -> repository.findByEmail(usernameOrEmail)) // Try finding by email if username not found
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

            // I could have directly check user password by hashing reverse and password match here
            // password check UserDetails = loadByUsername password check
            // throws BadCredentialsException
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), password));

            return jwtService.generateToken(user); // Return only the token
        }
        catch (Exception e){
            // General Error Goes to Global Exception Handler and Response Entity is thorwn
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage()); // Return HTTP 500 Internal Server Error
        }
    }

    // Authenticate a user and return the JWT token
    public String test(String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new BadRequestException("Auth Header Invalid");
            }
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            // Validate the token
            if (!jwtService.validateToken(token)) {
                throw new BadCredentialsException("Invalid or Expired Token");
            }

            // Extract username from token
            String username = jwtService.extractUsername(token);
            return username;
        }
        catch (Exception e){
            // General Error Goes to Global Exception Handler and Response Entity is thorwn
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage()); // Return HTTP 500 Internal Server Error
        }
    }

}
