package com.java.bank_rest.config;

import com.java.bank_rest.security.JwtAuthFilter;
import com.java.bank_rest.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/cards/create").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/api/cards/update/*/status",
                                "api/cards/block/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "api/cards/delete/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "api/cards").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "api/cards/user/*").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "api/cards/balance/*").hasRole("USER")

                        .requestMatchers(HttpMethod.POST, "api/cards/transfer/*").hasRole("USER")

                        .requestMatchers(HttpMethod.PATCH, "/api/cards/request/*").hasRole("USER")

                        .requestMatchers(HttpMethod.PATCH, "/user/block/*").hasRole("ADMIN")

                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

