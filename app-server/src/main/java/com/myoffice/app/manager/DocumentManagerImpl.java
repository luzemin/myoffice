package com.myoffice.app.manager;

import com.myoffice.app.constant.Constants;
import com.onlyoffice.manager.document.DefaultDocumentManager;
import com.onlyoffice.manager.settings.SettingsManager;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DocumentManagerImpl extends DefaultDocumentManager {
    public DocumentManagerImpl(final SettingsManager settingsManager) {
        super(settingsManager);
    }

    @Override
    public String getDocumentKey(final String fileId, final boolean embedded) {
        return String.valueOf(fileId.hashCode());
    }

    @Override
    public String getDocumentName(final String fileId) {
        File dir = new File(Constants.FILE_DIR + fileId);
        File file = dir.listFiles()[0];
        return file.getName();
    }
}
