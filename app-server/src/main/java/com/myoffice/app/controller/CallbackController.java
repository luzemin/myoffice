package com.myoffice.app.controller;

import com.onlyoffice.model.documenteditor.Callback;
import com.onlyoffice.service.documenteditor.callback.CallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ResponseBody
@RestController
public class CallbackController {

    @Autowired
    private CallbackService callbackService;

    @PostMapping("/callback")
    public String callback(@RequestBody final Callback callback,String fileId) {
        try {
            Callback verifiedCallback =  callbackService.verifyCallback(callback, null);

            callbackService.processCallback(verifiedCallback, fileId);
            return "{\"error\": 0}";
        } catch (Exception e) {
            return "{\"error\": 1}";
        }
    }
}
