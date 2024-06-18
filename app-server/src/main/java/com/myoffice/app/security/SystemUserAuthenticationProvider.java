package com.myoffice.app.security;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

public class SystemUserAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public boolean supports(Class<?> authentication)
    {
        return SystemUserAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
