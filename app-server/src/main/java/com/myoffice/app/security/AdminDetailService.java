package com.myoffice.app.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myoffice.app.mapper.AdminMapper;
import com.myoffice.app.model.domain.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AdminDetailService implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("name", username));
    }
}
