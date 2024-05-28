package com.myoffice.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myoffice.app.common.R;
import com.myoffice.app.model.domain.Task;
import com.myoffice.app.model.request.TaskRequest;

public interface TaskService extends IService<Task> {

    R createTask(TaskRequest request);

    R getTask(int userId);
}
