//// Security Congfig
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final UserDetailsServiceImplementation userDetailsServiceImplementation;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    public SecurityConfig(UserDetailsServiceImplementation userDetailsServiceImplementation,
//            JwtAuthenticationFilter jwtAuthenticationFilter) {
//        this.userDetailsServiceImplementation = userDetailsServiceImplementation;
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(
//                        req -> req
//                                .requestMatchers("/dashboard/**").hasAnyAuthority("ADMIN", "MANAGER", "USER")
//                                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "MANAGER")
//                                .requestMatchers("/manager/**").hasAuthority("MANAGER")
//                                .requestMatchers("/**")
//                                .permitAll()
//                                .anyRequest()
//                                .authenticated())
//                .userDetailsService(
//                        userDetailsServiceImplementation)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
//            throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//}
//
//// CORS Congfig
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Value("${frontend.dns}")
//    private String serverName;
//
//    @Value("${server.port}")
//    private String serverPort;
//
//    @Value("${front.end.port}")
//    private String frontEndPort;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://" + serverName + ":" + frontEndPort) // or use "*" to allow all origins
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
//}
//
////JwtAuthenticationFilter
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//
//    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImplementation userDetailsService) {
//        this.jwtService = jwtService;
//        this.userDetailsService = userDetailsService;
//    }
//
//    private final UserDetailsServiceImplementation userDetailsService;
//
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain)
//            throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String token = authHeader.substring(7);
//        String username = jwtService.extractUsername(token);
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            if (jwtService.isValid(token, userDetails)) {
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
//
//// UserDetailsServiceImplementation
//@Service
//public class UserDetailsServiceImplementation implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    public UserDetailsServiceImplementation(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUserName(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Name doesn't exist!!"));
//    }
//
//    public UserDetails loadUserByEmailId(String emailId) throws UsernameNotFoundException {
//        return userRepository.findByEmailId(emailId)
//                .orElseThrow(() -> new UsernameNotFoundException("Email ID doesn't exist!!"));
//    }
//}
//
////User Models using UserDetails from Security
//@Entity
//@Table(name = "users_mock_server")
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class User implements UserDetails {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private long id;
//
//    @Column(name = "user_name", unique = true)
//    private String userName;
//
//    @Column(name = "email_id", unique = true)
//    private String emailId;
//
//    @Column(name = "password")
//    private String password;
//
//    @Column(name = "is_active")
//    private int isActive;
//
//    @Column(name = "first_name")
//    private String firstName;
//
//    @Column(name = "last_name")
//    private String lastName;
//
//    @Enumerated(value = EnumType.STRING)
//    private Role role;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(role.name()));
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return userName;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getEmailId() {
//        return emailId;
//    }
//
//    public void setEmailId(String emailId) {
//        this.emailId = emailId;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public int getIsActive() {
//        return isActive;
//    }
//
//    public void setIsActive(int isActive) {
//        this.isActive = isActive;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", userName='" + userName + '\'' +
//                ", emailId='" + emailId + '\'' +
//                ", password='" + password + '\'' +
//                ", isActive=" + isActive +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", role=" + role +
//                '}';
//    }
//}
//
////UserRepo using JPA for ORM
//public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByUserName(String userName);
//
//    Optional<User> findByEmailId(String emailId);
//
//    @Query("Select u from User u where u.userName = ?1 and u.password = ?2")
//    Optional<User> loadByUserName(String userName, String password);
//}
//
////AuthenticationService
//@Service
//public class AuthenticationService {
//    private final UserRepository repository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//
//    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
//            AuthenticationManager authenticationManager) {
//        this.repository = repository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtService = jwtService;
//        this.authenticationManager = authenticationManager;
//    }
//
//    public AuthenticationResponse register(User request) {
//        User user = new User();
//        user.setEmailId(request.getEmailId());
//        user.setIsActive(request.getIsActive());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(request.getRole());
//        user.setUserName(request.getUsername());
//        user = repository.save(user);
//        String token = jwtService.generateToken(user);
//        return new AuthenticationResponse(token);
//    }
//
//    public AuthenticationResponse authenticate(User request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()));
//        User user = repository.findByUserName(request.getUsername()).orElseThrow();
//        String token = jwtService.generateToken(user);
//        return new AuthenticationResponse(token);
//    }
//
//    public AuthenticationResponse validate(String token) {
//        if (jwtService.validateToken(token)) {
//            return new AuthenticationResponse("Valid");
//        }
//        return new AuthenticationResponse("Invalid");
//    }
//}
//
////JWT Service Methods
//@Service
//public class JwtService {
//
//    @Value("${jwt.secret.key}")
//    private String secret;
//
//    public String generateToken(User user) {
//        return Jwts.builder().subject(user.getUsername()).issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)).signWith(getSigningKey())
//                .compact();
//    }
//
//    public boolean isValid(String token, UserDetails user) {
//        String userName = extractUsername(token);
//        return (userName.equals(user.getUsername()) && !isTokenExpired(token));
//    }
//
//    public boolean validateToken(String token) {
//        return !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
//    }
//
//    private SecretKey getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//}
