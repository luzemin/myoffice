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
import com.myoffice.app.model.request.TaskSearchCriteria;
import com.myoffice.app.model.response.TaskResponse;
import com.myoffice.app.security.user.UserContext;
import com.myoffice.app.service.TaskService;
import com.myoffice.app.utils.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.myoffice.app.constant.Constants.*;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserContext userContext;

    @Override
    public R createTask(TaskRequest request) {
        if (StringUtils.isBlank(request.getName())) {
            return R.error("task name is empty");
        }
        if (request.getOwner() == null) {
            return R.error("task owner is empty");
        }
        if (StringUtils.isBlank(request.getAssignee())) {
            return R.error("task assignee is empty");
        }
        if (request.getEndDate() == null) {
            return R.error("task end date is empty");
        }

        request.setStartDate(Date.from(Instant.now()));
        request.setStatus(0);

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

    @Override
    public R editTask(TaskRequest request) {
        Task targetTask = taskMapper.selectById(request.getId());
        if (targetTask == null) {
            return R.error("task does not exist");
        }

        if (!allowEdit(targetTask)) {
            return R.error("no permission to edit task");
        }

        Task entity = new Task();
        BeanUtils.copyProperties(request, entity);
        int result = taskMapper.updateById(entity);
        if (result > 0) {
            return R.success("success");
        }

        return R.error("failed to edit task");
    }

    @Override
    public R queryTask(TaskSearchCriteria searchCriteria) {
        Integer userId = userContext.getCurrentUserId();
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();

        //模糊查询task name
        if (StringUtils.isNotBlank(searchCriteria.getName())) {
            queryWrapper.like("task.name", searchCriteria.getName());
        }

        //查询状态
        if (searchCriteria.getStatus() != null && searchCriteria.getStatus() >= 0) {
            queryWrapper.eq("task.status", searchCriteria.getStatus());
        }

        //查询结束时间
        if (searchCriteria.getEndDate() != null) {
            String endDate = DateFormatUtils.format(searchCriteria.getEndDate(), DATE_FORMAT);
            String applySql = "task.end_date <= date '" + endDate + "'";
            queryWrapper.apply(applySql);
        }

        //我创建的
        if (Boolean.TRUE.equals(searchCriteria.getIsOwner())) {
            queryWrapper.eq("task.owner", userId);
        }

        //指派给我的
        if (Boolean.TRUE.equals(searchCriteria.getIsAssignee())) {
            String applySql = "FIND_IN_SET(" + userId + ", task.assignee)";
            queryWrapper.apply(applySql);
        }

        //默认查询：我创建的或者指派给我的
        if (searchCriteria.getIsOwner() == null && searchCriteria.getIsAssignee() == null) {
            queryWrapper.and(c -> c.apply("FIND_IN_SET(" + userId + ", task.assignee)")
                    .or().eq("task.owner", userId));
        }

        //排序
        queryWrapper.orderBy(true, false, "task.id");

        //分组
        queryWrapper.groupBy(List.of("task.id", "u1.username"));

        //默认单表分页查询
        //IPage<Task> result = taskMapper.selectPage(page, queryWrapper);

        //默认单表全部查询
        //List<Task> result = taskMapper.selectList(queryWrapper);

        //自定义多表联查分页实现
        IPage<TaskResponse> result = taskMapper
                .selectTask(new Page<>(searchCriteria.getPage(), searchCriteria.getPageSize()), queryWrapper);

        return R.success("success", result);
    }

    private boolean allowEdit(Task task) {
        Integer userId = userContext.getCurrentUserId();

        boolean isOwner = task.getOwner().equals(userId);
        boolean isAssignee = Arrays.stream(task.getAssignee().split(","))
                .map(Integer::parseInt)
                .anyMatch(id -> id.equals(userId));

        return isOwner || isAssignee;
    }

    private String createBlankTemplate(String taskName, String fileFormat) throws IOException {
        String fileId = RandomUtils.code();
        File filesDirectory = new File(Constants.FILE_DIR + fileId);
        if (!filesDirectory.exists()) {
            filesDirectory.mkdirs();
        }

        String fileFullPath = filesDirectory + "/" + taskName + "." + fileFormat;
        try (OutputStream outStream = new FileOutputStream(fileFullPath)) {
            InputStream is = new ClassPathResource(FILE_TEMPLATE_PATH + FILE_TEMPLATE_NAME + DOT + fileFormat).getInputStream();
            outStream.write(is.readAllBytes());
        }

        return fileId;
    }
}