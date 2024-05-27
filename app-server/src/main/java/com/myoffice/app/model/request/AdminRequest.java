package com.myoffice.app.model.request;

import lombok.Data;

@Data
public class AdminRequest {
    private Integer id;

    private String username;

    private String password;
}
