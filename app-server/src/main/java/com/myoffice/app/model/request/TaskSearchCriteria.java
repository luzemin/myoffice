package com.myoffice.app.model.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TaskSearchCriteria {

    private String name;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    private Boolean isOwner;

    private Boolean isAssignee;

    private Integer status;

    private Integer page = 1;

    private Integer pageSize = 5;
}
