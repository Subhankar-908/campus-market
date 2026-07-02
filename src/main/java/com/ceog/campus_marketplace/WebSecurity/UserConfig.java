package com.ceog.campus_marketplace.WebSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class UserConfig {
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private JwtFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
                .csrf(csrf->csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth->auth
//                        .requestMatchers(
//                                "/auth/**",
//                                "/api/auth/**",
//                                "/products/**",
//                                "/categorys/**").permitAll()
//                                .requestMatchers("/chat/**").permitAll()
//                                .requestMatchers("/api/v1/rooms/**").permitAll()
//                                .requestMatchers("/users/allUser").hasAnyRole("ADMIN", "SUPER_ADMIN")
                                .requestMatchers(
                                        "/",
                                        "/index.html",
                                        "/Admin.html",
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/auth/**"
                                ).permitAll()
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
        ).addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception{
        AuthenticationManager authenticationManager=
                authenticationConfiguration.getAuthenticationManager();
        return authenticationManager;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder=
                new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedOriginPatterns(Arrays.asList(
                "https://campus-market-frontend-6nl6.onrender.com"
        ));// ✅ Only this one
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));  // ✅ Add this
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
}

}
