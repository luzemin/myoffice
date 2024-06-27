package com.myoffice.app.security;

import com.myoffice.app.model.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    public Integer getCurrentUserId() {
        User user = getCurrentUser();
        return user == null ? null : user.getId();
    }

    public String getCurrentUserName() {
        User user = getCurrentUser();
        return user == null ? null : user.getUsername();
    }
}
