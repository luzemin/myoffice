package com.myoffice.app.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myoffice.app.mapper.AdminMapper;
import com.myoffice.app.mapper.UserMapper;
import com.myoffice.app.model.domain.Admin;
import com.myoffice.app.model.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserMapper userMapper;

    private final AdminMapper adminMapper;

    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {
        return username -> userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Bean(name = "adminDetailsService")
    public UserDetailsService adminDetailsService() {
        return username -> adminMapper.selectOne(new QueryWrapper<Admin>().eq("name", username));
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider dao1 = new DaoAuthenticationProvider();
        dao1.setUserDetailsService(userDetailsService());
        dao1.setPasswordEncoder(passwordEncoder());

        DaoAuthenticationProvider dao2 = new DaoAuthenticationProvider();
        dao2.setUserDetailsService(adminDetailsService());
        dao2.setPasswordEncoder(passwordEncoder());

        ProviderManager manager = new ProviderManager(dao1, dao2);
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}