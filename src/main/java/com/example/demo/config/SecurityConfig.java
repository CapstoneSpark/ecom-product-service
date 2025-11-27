//package com.example.demo.config;
//
//import java.util.Arrays;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import com.example.demo.security.JwtAuthenticationEntryPoint;
//import com.example.demo.security.JwtAuthenticationFilter;
//
///**
// * Security Configuration for Product Service
// *
// * Customize the getPublicEndpoints() method for each service.
// */
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Autowired
//    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    /**
//     * Password encoder bean
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * Main security filter chain
//     */
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            // Disable CSRF (not needed for stateless JWT)
//            .csrf(csrf -> csrf.disable())
//
//            // Configure CORS
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//
//            // Stateless session (JWT is stateless)
//            .sessionManagement(session ->
//                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//            // Exception handling
//            .exceptionHandling(exception ->
//                exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//
//            // Configure endpoint authorization
//            .authorizeHttpRequests(auth -> {
//                // Public endpoints - customize per service
//                auth.requestMatchers(getPublicEndpoints()).permitAll();
//
//                // All other endpoints require authentication
//                auth.anyRequest().authenticated();
//            })
//
//            // Add JWT filter before UsernamePasswordAuthenticationFilter
//            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    /**
//     * Define public endpoints - CUSTOMIZE THIS PER SERVICE
//     */
//    protected String[] getPublicEndpoints() {
//        // PRODUCT SERVICE EXAMPLE:
//        return new String[]{
//            "/api/products",              // List all products (public)
//            "/api/products/*",            // View product details (public)
//            "/api/products/search/**",    // Search products (public)
//            "/api/categories",            // List categories (public)
//            "/api/categories/*",          // View category (public)
//            "/actuator/health",           // Health check
//            "/api/health",
//            "/swagger-ui/**",             // Swagger UI
//            "/v3/api-docs/**"             // API docs
//        };
//
//        // Override this method in service-specific config if you need different public endpoints.
//    }
//
//    /**
//     * CORS configuration
//     */
//    protected CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList(
//            "http://localhost:3000",      // React dev server
//            "http://localhost:5173",      // Vite dev server
//            "http://localhost:8080"       // Local testing
//        ));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//}
