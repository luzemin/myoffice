package com.myoffice.app.security.user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SystemUserAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public SystemUserAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public SystemUserAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
