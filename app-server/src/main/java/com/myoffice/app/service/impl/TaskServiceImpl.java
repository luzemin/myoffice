package com.myoffice.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myoffice.app.common.R;
import com.myoffice.app.constant.Constants;
import com.myoffice.app.mapper.TaskMapper;
import com.myoffice.app.model.domain.Task;
import com.myoffice.app.model.request.TaskRequest;
import com.myoffice.app.model.response.TaskResponse;
import com.myoffice.app.service.TaskService;
import com.myoffice.app.utils.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;

import static com.myoffice.app.constant.Constants.*;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public R createTask(TaskRequest request) {
        //需要校验当前登录用户是否可以创建
        //需要校验task的必填项
        try {
            if ("BLANK".equals(request.getTemplateSource())) {
                String taskName = request.getName();
                String fileId = createBlankTemplate(taskName, request.getTemplateFormat());
                request.setTemplateName(taskName);
                request.setTemplate(fileId);
            }
        } catch (IOException e) {
            return R.error("failed to create template for task");
        }

        Task entity = new Task();
        BeanUtils.copyProperties(request, entity);
        int result = taskMapper.insert(entity);
        if (result > 0) {
            return R.success("success");
        }

        return R.error("failed to create new task");
    }

    private String createBlankTemplate(String taskName, String fileFormat) throws IOException {
        String fileId = RandomUtils.code();
        File filesDirectory = new File(Constants.FILE_DIR + fileId);
        if (!filesDirectory.exists()) {
            filesDirectory.mkdir();
        }

        String fileFullPath = filesDirectory + "/" + taskName + "." + fileFormat;
        try (OutputStream outStream = new FileOutputStream(fileFullPath)) {
            InputStream is = new ClassPathResource(FILE_TEMPLATE_PATH + FILE_TEMPLATE_NAME + DOT + fileFormat).getInputStream();
            outStream.write(is.readAllBytes());
        }

        return fileId;
    }

    @Override
    public R editTask(TaskRequest request) {
        //需要校验当前登录用户是否可以编辑
        Task entity = new Task();
        BeanUtils.copyProperties(request, entity);
        int result = taskMapper.updateById(entity);
        if (result > 0) {
            return R.success("success");
        }

        return R.error("failed to edit task");
    }

    //条件检索：taskname enddate status
    //分页
    //联查：联查用户表显示创建人和执行人user name
    @Override
    public R queryTask(int userId, TaskRequest request) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();

        //模糊查询task name
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.like("task.name", request.getName());
        }

        queryWrapper.and(c -> c.eq("task.assignee", userId).or().eq("task.owner", userId))
                .orderBy(true, false, "task.id");

        //默认单表分页查询
        //IPage<Task> result = taskMapper.selectPage(page, queryWrapper);

        //默认单表全部查询
        //List<Task> result = taskMapper.selectList(queryWrapper);

        //自定义多表联查分页实现
        IPage<TaskResponse> result = taskMapper.selectTask(new Page<>(request.getPage(), 5), queryWrapper);

        return R.success("success", result);
    }
}