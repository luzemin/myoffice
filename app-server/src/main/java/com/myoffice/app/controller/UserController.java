package com.myoffice.app.controller;

import com.myoffice.app.common.R;
import com.myoffice.app.model.request.AdminRequest;
import com.myoffice.app.model.request.UserRequest;
import com.myoffice.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/auth/user/login")
    public R loginStatus(@RequestBody UserRequest userRequest) {
        return userService.login(userRequest);
    }
}
