package com.myoffice.app.security;

import com.myoffice.app.security.admin.AdminAuthenticationProvider;
import com.myoffice.app.security.admin.AdminDetailService;
import com.myoffice.app.security.user.SystemUserAuthenticationProvider;
import com.myoffice.app.security.user.SystemUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurations {

    private final SystemUserDetailService systemUserDetailService;
    private final AdminDetailService adminDetailService;
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public AuthenticationManager authenticationManager() {
        SystemUserAuthenticationProvider userAuthProvider = new SystemUserAuthenticationProvider();
        userAuthProvider.setUserDetailsService(systemUserDetailService);
        userAuthProvider.setPasswordEncoder(passwordEncoder());

        AdminAuthenticationProvider adminAuthProvider = new AdminAuthenticationProvider();
        adminAuthProvider.setUserDetailsService(adminDetailService);
        adminAuthProvider.setPasswordEncoder(passwordEncoder());

        ProviderManager manager = new ProviderManager(userAuthProvider, adminAuthProvider);
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/password/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}