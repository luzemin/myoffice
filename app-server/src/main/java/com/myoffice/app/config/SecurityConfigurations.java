package com.myoffice.app.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myoffice.app.mapper.AdminMapper;
import com.myoffice.app.mapper.UserMapper;
import com.myoffice.app.model.domain.Admin;
import com.myoffice.app.model.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurations {

    private final UserMapper userMapper;
    private final AdminMapper adminMapper;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private static final String[] WHITE_LIST_URL = {};

    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {
        return username -> userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Bean(name = "adminDetailsService")
    public UserDetailsService adminDetailsService() {
        return username -> adminMapper.selectOne(new QueryWrapper<Admin>().eq("name", username));
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
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}