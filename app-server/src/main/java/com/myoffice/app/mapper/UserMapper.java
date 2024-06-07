package com.myoffice.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myoffice.app.model.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

}
