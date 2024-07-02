package com.myoffice.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myoffice.app.security.user.SystemUserAuthenticationToken;
import com.myoffice.app.common.R;
import com.myoffice.app.mapper.UserMapper;
import com.myoffice.app.model.domain.User;
import com.myoffice.app.model.request.UserRequest;
import com.myoffice.app.model.response.UserResponse;
import com.myoffice.app.service.UserService;
import com.myoffice.app.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.myoffice.app.constant.Constants.DEFAULT_PASSWORD;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", userName);
//        queryWrapper.eq("password", password);
//        User user = userMapper.selectOne(queryWrapper);

        try {
            //利用spring security机制验证用户名密码
            Authentication authentication = authenticationManager.authenticate(
                    new SystemUserAuthenticationToken(userName, password)
            );

            //User user = (User) authenticate.getPrincipal();
            UserResponse responseData = UserResponse.builder()
                    .username(userName)
                    .token(jwtUtil.generateToken(userName))
                    .build();

            return R.success("success", responseData);

        } catch (AuthenticationException ex) {

            return R.error("user name or password is incorrect");
        }
    }

    @Override
    public R create(UserRequest userRequest) {
        if (StringUtils.isBlank(userRequest.getUsername())) {
            return R.error("user name is empty");
        }

        if (StringUtils.isBlank(userRequest.getPassword())) {
            userRequest.setPassword(DEFAULT_PASSWORD);
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        int rows = userMapper.insert(user);
        if (rows > 0) {
            return R.success("success");
        }
        return R.error("failed to create user");
    }

    @Override
    public R getAllUsers() {
        List<User> allUsers = userMapper.selectList(new QueryWrapper<>());
        List<UserResponse> users = allUsers
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), null))
                .collect(Collectors.toList());
        return R.success("success", users);
    }
}
