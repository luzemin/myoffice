package com.myoffice.app.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@TableName(value = "task")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private String assignee;
    private Integer status;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}