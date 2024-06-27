package com.myoffice.app;

import com.myoffice.app.common.R;
import com.myoffice.app.service.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void testGetAllUsers() {
        R result = userService.getAllUsers();
        assertNotNull(result);
        assertEquals("success", result.getMessage());
    }
}
