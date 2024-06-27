package com.myoffice.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myoffice.app.common.R;
import com.myoffice.app.model.domain.User;
import com.myoffice.app.model.request.UserRequest;

public interface UserService extends IService<User> {

    R login(UserRequest userRequest);

    R create(UserRequest userRequest);

    R getAllUsers();
}
