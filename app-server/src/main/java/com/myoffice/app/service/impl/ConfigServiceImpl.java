package com.myoffice.app.service.impl;

import com.onlyoffice.manager.document.DocumentManager;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.common.User;
import com.onlyoffice.model.documenteditor.config.EditorConfig;
import com.onlyoffice.model.documenteditor.config.document.Permissions;
import com.onlyoffice.model.documenteditor.config.document.Type;
import com.onlyoffice.model.documenteditor.config.editorconfig.Customization;
import com.onlyoffice.model.documenteditor.config.editorconfig.Mode;
import com.onlyoffice.model.documenteditor.config.editorconfig.customization.Customer;
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

    @Override
    public EditorConfig getEditorConfig(String fileId, Mode mode, Type type) {
        EditorConfig editorConfig = super.getEditorConfig(fileId, mode, type);
        editorConfig.setLang("zh");
        return editorConfig;
    }

    @Override
    public Customization getCustomization(String fileId) {
        Customization customization = super.getCustomization(fileId);
        //定制：https://api.onlyoffice.com/zh/editors/faq/customizing
        //付费版本才会生效
        customization.setCustomer(Customer.builder().logo("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png").build());
        return customization;
    }
}
