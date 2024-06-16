package com.myoffice.app.controller;

import com.myoffice.app.common.R;
import com.myoffice.app.model.request.AdminRequest;
import com.myoffice.app.service.AdminService;
import com.myoffice.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/auth/admin/login")
    public R adminLogin(@RequestBody AdminRequest adminRequest) {
        return adminService.adminLogin(adminRequest);
    }

    @GetMapping("/api/admin/user/all")
    public R getAllUsers() {
        return userService.getAllUsers();
    }
}