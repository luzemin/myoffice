package com.myoffice.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myoffice.app.common.R;
import com.myoffice.app.mapper.UserMapper;
import com.myoffice.app.model.domain.User;
import com.myoffice.app.model.request.UserRequest;
import com.myoffice.app.model.response.UserResponse;
import com.myoffice.app.service.UserService;
import com.myoffice.app.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public R login(UserRequest userRequest) {
        String userName = userRequest.getUsername();
        String password = userRequest.getPassword();

        if (StringUtils.isBlank(userName)) {
            return R.error("user name is empty");
        }

        if (StringUtils.isBlank(password)) {
            return R.error("password is empty");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userName);
        queryWrapper.eq("password", password);
        User user = userMapper.selectOne(queryWrapper);

        if (null != user) {
            UserResponse responseData = UserResponse.builder()
                    .username(userName)
                    .token(jwtUtil.generateToken(userName))
                    .build();
            return R.success("success", responseData);
        }

        return R.error("user name or password is incorrect");
    }
}