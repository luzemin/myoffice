package com.myoffice.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myoffice.app.common.R;
import com.myoffice.app.constant.Constants;
import com.myoffice.app.mapper.TaskMapper;
import com.myoffice.app.model.domain.Task;
import com.myoffice.app.model.request.TaskRequest;
import com.myoffice.app.service.TaskService;
import com.myoffice.app.utils.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public R createTask(TaskRequest request) {
        try {
            if ("BLANK".equals(request.getTemplateSource())) {
                String fileId = createBlankTemplate(request.getName(), request.getTemplateFormat());
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
        File savedFile = new File(filesDirectory + "/" + taskName + "." + fileFormat);
        OutputStream outStream = new FileOutputStream(savedFile);
        InputStream is = new ClassPathResource("/assets/document-templates/template." + fileFormat).getInputStream();
        outStream.write(is.readAllBytes());

        return fileId;
    }

    @Override
    public R editTask(TaskRequest request) {
        Task entity = new Task();
        BeanUtils.copyProperties(request, entity);
        int result = taskMapper.updateById(entity);
        if (result > 0) {
            return R.success("success");
        }

        return R.error("failed to edit task");
    }

    @Override
    public R queryTask(int userId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("assignee", userId)
                .or()
                .eq("owner", userId);
        List<Task> result = taskMapper.selectList(queryWrapper);

        return R.success("success", result);
    }
}