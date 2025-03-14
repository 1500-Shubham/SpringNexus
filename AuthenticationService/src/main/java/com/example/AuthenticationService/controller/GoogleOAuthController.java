package com.example.AuthenticationService.controller;

import com.example.AuthenticationService.service.GoogleOAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class GoogleOAuthController {

    @Autowired
    private GoogleOAuthService googleOAuthService;


    @GetMapping("/loginOrRegister")
    public ResponseEntity<String> loginWithGoogle(OAuth2AuthenticationToken oauthToken) {
        if (oauthToken == null) {
            // OAuth2 login hasn't happened yet, manually trigger the OAuth2 login process
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "/oauth2/authorization/google") // This triggers OAuth2 login with Google
                    .build();
        }
        OAuth2User oauthUser = oauthToken.getPrincipal();
        // Handle user login or registration and generate the JWT token
        String jwtToken = googleOAuthService.handleGoogleOAuth(oauthUser);
        // Return the JWT token as the response
        return ResponseEntity.ok("JWT Token: " + jwtToken);
    }

    @GetMapping("/loginFailure")
    public ResponseEntity<String> handleOAuth2LoginFailure(HttpServletRequest request) {
        String errorMessage = (String) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        if (errorMessage == null) {
            errorMessage = "OAuth2 login failed. Please try again.";
        }

        // Manually throwing OAuth2AuthenticationException
        // this is taken care in Global exception Handler
        throw new OAuth2AuthenticationException(new OAuth2Error("oauth2_login_error", errorMessage, null));
    }

}
