package com.myoffice.app.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class TaskRequest {
    private Integer id;
    private String name;
    private String description;
    private String template;
    //1. docx 2. xlsx 3. pptx
    private String templateFormat;
    //1.选择了模板库中的模板 SELECT 2.上传了新的模板 UPLOAD 3.空白模板 BLANK
    private String templateSource;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate  = Date.from(Instant.now());
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    private Integer owner;
    private Integer assignee;
    private Integer status = 0;
}
