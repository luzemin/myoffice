package com.myoffice.app.security.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myoffice.app.mapper.UserMapper;
import com.myoffice.app.model.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SystemUserDetailService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }
}
