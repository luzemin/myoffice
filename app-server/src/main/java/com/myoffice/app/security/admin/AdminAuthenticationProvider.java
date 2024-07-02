package com.myoffice.app.security.admin;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

public class AdminAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public boolean supports(Class<?> authentication)
    {
        return AdminAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
