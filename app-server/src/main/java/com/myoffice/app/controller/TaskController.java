package com.myoffice.app.controller;

import com.myoffice.app.common.R;
import com.myoffice.app.model.domain.User;
import com.myoffice.app.model.request.TaskRequest;
import com.myoffice.app.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/api/task/create")
    public R create(@RequestBody TaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/api/task/query")
    public R query(Authentication auth, TaskRequest request) {
        User currentUser = (User) auth.getPrincipal();
        return taskService.queryTask(currentUser.getId(),request);
    }

    @PostMapping("/api/task/edit")
    public R edit(@RequestBody TaskRequest request) {
        return taskService.editTask(request);
    }
}