package com.myoffice.app.service.impl;

import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.common.User;
import com.onlyoffice.model.documenteditor.config.document.Permissions;
import com.onlyoffice.service.documenteditor.config.DefaultConfigService;
import org.springframework.stereotype.Component;

@Component
public class ConfigServiceImpl extends DefaultConfigService {
    public ConfigServiceImpl(final DocumentManager documentManager, final UrlManager urlManager,
                             final JwtManager jwtManager, final SettingsManager settingsManager) {
        super(documentManager, urlManager, jwtManager, settingsManager);
    }

    @Override
    public Permissions getPermissions(final String fileId) {
        Permissions permissions = Permissions.builder()
                .edit(true)
                .build();
        return permissions;
    }

    @Override
    public User getUser() {
        //return current login user
        return User.builder().name("Timothy" + Math.random() * 100).build();
    }
}
