package com.myoffice.app.manager;

import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UrlMangerImpl extends DefaultUrlManager {
    @Autowired
    private HttpServletRequest request;

    public UrlMangerImpl(final SettingsManager settingsManager) {
        super(settingsManager);
    }

    @Override
    public String getFileUrl(final String fileId) {
        return getServerUrl() + "/api/file/download?fileId=" + fileId + "&token=" + request.getParameter("token");
    }

    @Override
    public String getCallbackUrl(final String fileId) {
        return getServerUrl() + "/api/onlyoffice/callback?fileId=" + fileId + "&token=" + request.getParameter("token");
    }

    private String getServerUrl() {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
    }
}
