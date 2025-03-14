package com.example.AuthenticationService.components;

import com.example.AuthenticationService.ErrorHandling.JWTCustomException;
import com.example.AuthenticationService.service.JwtService;
import com.example.AuthenticationService.service.UserDetailsServiceImplementation;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImplementation userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImplementation userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {


            String authHeader = request.getHeader("Authorization");
//            System.out.println("Authorization Header" + authHeader);
            // If Authorization header is missing, proceed without authentication
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                throw new JWTCustomException("Authorization header is missing or malformed");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7); // Remove "Bearer " prefix
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            // Since JWTFilter doesn't return any value like API do I need to use this to return
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"An unexpected error occurred during JWT validation Filter\"}");
        }
    }
}

