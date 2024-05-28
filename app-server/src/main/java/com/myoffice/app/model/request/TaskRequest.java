package com.myoffice.app.model.request;

import lombok.Data;

@Data
public class TaskRequest {
    private Integer id;

    private String name;

    private String template;
}
