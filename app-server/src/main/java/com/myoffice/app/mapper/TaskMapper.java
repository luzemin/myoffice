package com.myoffice.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myoffice.app.model.domain.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskMapper extends BaseMapper<Task> {

}
