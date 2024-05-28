package com.myoffice.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myoffice.app.common.R;
import com.myoffice.app.mapper.TaskMapper;
import com.myoffice.app.model.domain.Task;
import com.myoffice.app.model.request.TaskRequest;
import com.myoffice.app.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public R createTask(TaskRequest request) {
        Task entity = new Task();
        BeanUtils.copyProperties(request, entity);
        int result = taskMapper.insert(entity);
        if (result > 0) {
            return R.success("success");
        }

        return R.error("failed to create new task");
    }

    @Override
    public R getTask(int userId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner", userId);
        List<Task> result = taskMapper.selectList(queryWrapper);

        return R.success("success", result);
    }
}
