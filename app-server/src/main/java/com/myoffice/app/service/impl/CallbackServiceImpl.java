/**
 * (c) Copyright Ascensio System SIA 2024
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myoffice.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyoffice.manager.security.JwtManager;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.model.documenteditor.Callback;
import com.onlyoffice.service.documenteditor.callback.DefaultCallbackService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

@Component
public class CallbackServiceImpl extends DefaultCallbackService {

    private final ObjectMapper mapper = new ObjectMapper();

    public CallbackServiceImpl(final JwtManager jwtManager, final SettingsManager settingsManager) {
        super(jwtManager, settingsManager);
    }


    @Override
    public void handlerEditing(final Callback callback, final String fileId) throws Exception {
        System.out.println("Editing...");
        System.out.println("Callback object: " + mapper.writeValueAsString(callback));
    }

    /*
     * 官方保存示例
     * https://api.onlyoffice.com/zh/editors/callback#java
     * */
    @Override
    public void handlerSave(final Callback callback, final String fileId) throws Exception {
        System.out.println("Saving...");
        System.out.println("Callback object: " + mapper.writeValueAsString(callback));

        URL url = new URL(callback.getUrl());
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
        InputStream stream = connection.getInputStream();

        Resource resource = new UrlResource(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("app_data/sample.docx"));
        String pathForSave = resource.getFile().getAbsolutePath();
        System.out.println("Saving to " + pathForSave);

        File savedFile = new File(pathForSave);
        try (FileOutputStream out = new FileOutputStream(savedFile)) {
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = stream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        }
        connection.disconnect();
        System.out.println("Save Succeed");
    }
}
