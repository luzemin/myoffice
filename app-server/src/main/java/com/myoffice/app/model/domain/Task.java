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
    private Date startDate;
    private Date endDate;
    private Integer owner;
    private Integer assignee;
    //0.未开始 1.进行中 2.已完成 3.关闭
    private Integer status = 0;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}