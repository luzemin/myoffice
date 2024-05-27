package com.myoffice.app;

import com.onlyoffice.manager.request.DefaultRequestManager;
import com.onlyoffice.manager.request.RequestManager;
import com.onlyoffice.manager.security.DefaultJwtManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.myoffice.app.mapper")
public class MyOfficeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyOfficeApplication.class, args);
    }

    @Bean
    public JwtManager jwtManager(final SettingsManager settingsManager) {
        return new DefaultJwtManager(settingsManager);
    }

    @Bean
    public RequestManager requestManager(final UrlManager urlManager, final JwtManager jwtManager,
                                         final SettingsManager settingsManager) {
        return new DefaultRequestManager(urlManager, jwtManager, settingsManager);
    }
}