package com.myoffice.app.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@TableName(value = "task")
@Data
public class Task {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String template;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
