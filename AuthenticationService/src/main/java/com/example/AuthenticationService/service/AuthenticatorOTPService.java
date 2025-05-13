package com.example.AuthenticationService.service;

import com.example.AuthenticationService.dto.AuthenticatorOtpDTO;
import com.example.AuthenticationService.dto.UserDTO;
import com.example.AuthenticationService.models.User;
import com.example.AuthenticationService.repository.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticatorOTPService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public AuthenticatorOTPService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = repository;
        this.encoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public ResponseEntity<?> verfiyAndLogin(AuthenticatorOtpDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        if (user.isFirstLogin()) {
            // First time login - generate QR
            GoogleAuthenticatorKey key = gAuth.createCredentials();
            user.setTotpSecret(key.getKey());
            user.setFirstLogin(false);
            userRepository.save(user);
            String qr = GoogleAuthenticatorQRGenerator.getOtpAuthURL("SpringNexus", user.getEmail(), key);
            return ResponseEntity.ok(Map.of("qrCodeUrl", qr));
        }
        boolean isValid = gAuth.authorize(user.getTotpSecret(),dto.getOtp());
        if (!isValid) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", jwt));
    }


}
