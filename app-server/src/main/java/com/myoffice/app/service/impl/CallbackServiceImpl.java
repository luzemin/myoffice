package com.myoffice.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myoffice.app.constant.Constants;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.documenteditor.Callback;
import com.onlyoffice.service.documenteditor.callback.DefaultCallbackService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
public class CallbackServiceImpl extends DefaultCallbackService {
    private final Logger logger = Logger.getLogger(getClass());
    private final ObjectMapper mapper = new ObjectMapper();

    public CallbackServiceImpl(final JwtManager jwtManager, final SettingsManager settingsManager) {
        super(jwtManager, settingsManager);
    }

    @Override
    public void handlerEditing(final Callback callback, final String fileId) throws Exception {
//        logger.info("Editing...");
//        logger.info("Callback object: " + mapper.writeValueAsString(callback));
    }

    /*
     * 官方保存示例
     * https://api.onlyoffice.com/zh/editors/callback#java
     * */
    @Override
    public void handlerSave(final Callback callback, final String fileId) throws Exception {
        saveFile(callback, fileId);
    }

    @Override
    public void handlerForcesave(Callback callback, String fileId) throws Exception {
        saveFile(callback, fileId);
    }

    private void saveFile(Callback callback, String fileId) throws IOException {
        URL url = new URL(callback.getUrl());
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
        InputStream stream = connection.getInputStream();

        File dir = new File(Constants.FILE_DIR + fileId);
        File savedFile = dir.listFiles()[0];

        try (FileOutputStream out = new FileOutputStream(savedFile)) {
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = stream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        }
        connection.disconnect();
    }
}