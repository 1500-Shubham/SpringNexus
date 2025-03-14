package com.example.AuthenticationService.config;

import com.example.AuthenticationService.components.JwtAuthenticationEntryPoint;
import com.example.AuthenticationService.components.JwtAuthenticationFilter;
import com.example.AuthenticationService.service.UserDetailsServiceImplementation;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/db/register", "/db/login","/oauth/loginOrRegister","oauth2/authorization/google","/oauth/loginFailure","/OTP/**").permitAll()
                        // All other requests need JWT authentication
                        .anyRequest().authenticated()

                )
                // "/oauth/loginOrRegister" will trigger Oauth Manually
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                // Oauth laga diya will be triggering manually
                // 1- /oauth/loginOrRegister then token null -> redirect /oauth2/authorization/google
                // 2- Spring oauth set hai toh success hoke -> redirect /oauth/loginOrRegister now token hai
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth/loginOrRegister", true)  // After OAuth2 login, redirect to /oauth/loginOrRegister
                        .failureUrl("/oauth/loginFailure")
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

//        AuthenticationManager: Coordinates the authentication process.
//        AuthenticationProvider: Actually performs the authentication (including database checks).
//        UserDetailsService: Loads user details from the database.

//        Steps internal working
        //UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( request.getUsername(), request.getPassword());
        // Authentication authentication = authenticationManager.authenticate(authenticationToken); // <-- calls DaoAuthenticationProvider internally
        //    if (authentication.isAuthenticated)



        // UserDetails loadUserByUsername function call karta to load user
//        public class UserDetailsServiceImplementation implements UserDetailsService {
//            private final UserRepository repository;
//
//            public UserDetailsServiceImplementation(UserRepository repository) {
//                this.repository = repository;
//            }
//
//            @Override
//            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//                return repository.findByEmail(email)
//                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//            }
//        }

        // if find by email ID then
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(), // Change from username to email
//                        request.getPassword()));
    }

    // is application properties not changing port then bean banake define
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setPort(8083);  // This will force the app to use port 8080
        return factory;
    }
}
