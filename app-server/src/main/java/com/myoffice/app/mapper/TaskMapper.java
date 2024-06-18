package com.myoffice.app.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.myoffice.app.model.domain.Task;
import com.myoffice.app.model.response.TaskResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskMapper extends BaseMapper<Task> {

    IPage<TaskResponse> selectTask(IPage page, @Param(Constants.WRAPPER) Wrapper<Task> queryWrapper);
}
