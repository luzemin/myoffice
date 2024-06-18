package com.myoffice.app.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@TableName(value = "task")
@Data
public class Task {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private String template;
    private String templateName;
    private String templateSource;
    private String templateFormat;
    private Date startDate;
    private Date endDate;
    private Integer owner;
    private Integer assignee;
    private Integer status = 0;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}