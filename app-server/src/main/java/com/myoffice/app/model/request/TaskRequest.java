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

    //模板，即file id
    private String template;

    //模板名称，即附件名称
    private String templateName;

    //1.docx
    // 2.xlsx
    // 3.pptx
    private String templateFormat;

    //1.选择了模板库中的模板 SELECT
    //2.上传了新的模板 UPLOAD
    //3.空白模板 BLANK
    private String templateSource;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate = Date.from(Instant.now());

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private Integer owner;

    private Integer assignee;

    //0.未开始
    //1.进行中
    //2.已完成
    //3.关闭
    private Integer status = 0;

    private Integer page = 1;
}
