package com.myoffice.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myoffice.app.common.R;
import com.myoffice.app.mapper.AdminMapper;
import com.myoffice.app.model.domain.Admin;
import com.myoffice.app.model.request.AdminRequest;
import com.myoffice.app.service.AdminService;
import com.myoffice.app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public R verityPasswd(AdminRequest adminRequest) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", adminRequest.getUsername());
        queryWrapper.eq("password", adminRequest.getPassword());

        Admin admin = adminMapper.selectOne(queryWrapper);
        if (null != admin) {
            var jwtToken = jwtUtil.generateToken(adminRequest.getUsername());
            return R.success("success", jwtToken);
        }

        return R.error("failed to login");
    }
}
