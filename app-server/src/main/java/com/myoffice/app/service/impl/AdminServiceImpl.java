package com.myoffice.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myoffice.app.common.R;
import com.myoffice.app.mapper.AdminMapper;
import com.myoffice.app.model.domain.Admin;
import com.myoffice.app.model.request.AdminRequest;
import com.myoffice.app.model.response.AdminResponse;
import com.myoffice.app.security.admin.AdminAuthenticationToken;
import com.myoffice.app.service.AdminService;
import com.myoffice.app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public R adminLogin(AdminRequest adminRequest) {
        try {
            authenticationManager.authenticate(
                    new AdminAuthenticationToken(adminRequest.getUsername(), adminRequest.getPassword())
            );
            AdminResponse responseData = AdminResponse.builder()
                    .username(adminRequest.getUsername())
                    .token(jwtUtil.generateToken(adminRequest.getUsername()))
                    .build();

            return R.success("success", responseData);

        } catch (AuthenticationException ex) {

            return R.error("failed to login");
        }
    }
}
