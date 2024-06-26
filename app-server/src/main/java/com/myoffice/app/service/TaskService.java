package com.myoffice.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myoffice.app.common.R;
import com.myoffice.app.model.domain.Task;
import com.myoffice.app.model.request.TaskRequest;
import com.myoffice.app.model.request.TaskSearchCriteria;

public interface TaskService extends IService<Task> {

    R createTask(TaskRequest request);

    R editTask(TaskRequest request);

    R queryTask(TaskSearchCriteria searchCriteria);
}