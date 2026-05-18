package com.myoffice.app.manager;

import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlMangerImpl extends DefaultUrlManager {
    @Autowired
    private HttpServletRequest request;

    @Value("${myoffice.internal-base-url:}")
    private String internalBaseUrl;

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
        // When deployed behind docker / reverse proxy, the browser-facing host
        // (e.g. localhost:3000) is unreachable from the DocumentServer container.
        // A configured internal base URL takes precedence so callback / file URLs
        // resolve via the container network.
        if (StringUtils.isNotBlank(internalBaseUrl)) {
            return StringUtils.removeEnd(internalBaseUrl, "/") + request.getContextPath();
        }
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
    }
}
