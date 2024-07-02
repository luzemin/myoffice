package com.myoffice.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/health")
    public String health() {
        return "\uD83D\uDE04 I'm fine, and you?";
    }

    @GetMapping("/password/{pwd}")
    public String password(@PathVariable("pwd") String plainText) {
        return passwordEncoder.encode(plainText);
    }
}
