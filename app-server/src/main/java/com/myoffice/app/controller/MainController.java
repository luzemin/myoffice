package com.myoffice.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.documenteditor.Config;
import com.onlyoffice.model.documenteditor.config.document.Type;
import com.onlyoffice.model.documenteditor.config.editorconfig.Mode;
import com.onlyoffice.service.documenteditor.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private ConfigService configService;
    @Autowired
    private UrlManager urlManager;

    @GetMapping("/editor")
    public String main(final Model model, String fileId) throws JsonProcessingException {
        Config config = configService.createConfig(fileId, Mode.EDIT, Type.DESKTOP);

        model.addAttribute("config", config);
        model.addAttribute("documentServerApiUrl", urlManager.getDocumentServerApiUrl());

        return "editor";
    }
}
