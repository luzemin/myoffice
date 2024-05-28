package com.myoffice.app.controller;

import com.myoffice.app.common.R;
import com.myoffice.app.model.request.TaskRequest;
import com.myoffice.app.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/task/creation")
    public R creation(@RequestBody TaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/task/retrieval")
    public R retrieval(@RequestBody int userId) {

        return taskService.getTask(userId);
    }
}