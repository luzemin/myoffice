package com.myoffice.app.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TaskRequest {
    private Integer id;
    private String name;
    private String template;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    private Integer owner;
    private Integer assignee;
    private Integer status;
}
