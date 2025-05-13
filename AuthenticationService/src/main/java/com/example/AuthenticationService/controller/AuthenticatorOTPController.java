package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.dto.AuthenticatorOtpDTO;
import com.example.AuthenticationService.service.AuthenticatorOTPService;
import com.example.AuthenticationService.service.DBAuthenticationService;
import com.example.AuthenticationService.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/authenticatorOTP")
public class AuthenticatorOTPController {

    @Autowired
    private AuthenticatorOTPService authOTPService;

    public AuthenticatorOTPController(AuthenticatorOTPService authOTPService) {
        this.authOTPService = authOTPService;
    }

    @PostMapping("/verfiyAndLogin")
    public ResponseEntity<?> login(@RequestBody AuthenticatorOtpDTO dto) {
        return authOTPService.verfiyAndLogin(dto);
    }
}
