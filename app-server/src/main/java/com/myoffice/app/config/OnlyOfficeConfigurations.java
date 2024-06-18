package com.myoffice.app.config;

import com.onlyoffice.manager.request.DefaultRequestManager;
import com.onlyoffice.manager.request.RequestManager;
import com.onlyoffice.manager.security.DefaultJwtManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyOfficeConfigurations {

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