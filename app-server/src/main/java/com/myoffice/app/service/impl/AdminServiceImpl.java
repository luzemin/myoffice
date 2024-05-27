package com.myoffice.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myoffice.app.common.R;
import com.myoffice.app.mapper.AdminMapper;
import com.myoffice.app.model.domain.Admin;
import com.myoffice.app.model.request.AdminRequest;
import com.myoffice.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public R verityPasswd(AdminRequest adminRequest) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",adminRequest.getUsername());
        queryWrapper.eq("password",adminRequest.getPassword());
        if (adminMapper.selectCount(queryWrapper) > 0) {
            return R.success("登录成功");
        } else {
            return R.error("用户名或密码错误");
        }
    }
}
