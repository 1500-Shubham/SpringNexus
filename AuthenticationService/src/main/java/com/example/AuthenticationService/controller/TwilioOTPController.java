package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.models.User;
import com.example.AuthenticationService.repository.UserRepository;
import com.example.AuthenticationService.service.JwtService;
import com.example.AuthenticationService.service.TwilioOTPService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/TwilioOTP")
public class TwilioOTPController {

    private final TwilioOTPService twilioOtpService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    // Constructor Injection for OTPService, UserRepository, and JwtService
    public TwilioOTPController(TwilioOTPService twilioOtpService, UserRepository userRepository, JwtService jwtService) {
        this.twilioOtpService = twilioOtpService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/sendOtp")
    public String sendOtp(@RequestParam String phoneNumber) {
        String otp = twilioOtpService.generateOtp();
        twilioOtpService.sendOtp(phoneNumber, otp);
        return "OTP sent to your phone number!";
    }

    @PostMapping("/verifyOtp")
    public String verifyOtpAndLogin(@RequestParam String phoneNumber, @RequestParam String otp) {
        // Validate OTP first
        boolean isOtpValid = twilioOtpService.validateOtp(phoneNumber, otp);
        if (!isOtpValid) {
            return "Invalid OTP or OTP has expired.";
        }

        Optional<User> userOpt = userRepository.findByPhone(phoneNumber);
        if (userOpt.isEmpty()) {
            return "User does not exist. Please register first.";
        }

        // Generate JWT token for the authenticated user
        User user = userOpt.get();
        String token = jwtService.generateToken(user);
        return "Login successful! JWT Token: " + token;
    }
}

