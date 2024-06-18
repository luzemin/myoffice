package com.myoffice.app.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myoffice.app.model.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse extends Task {
    private String ownerName;
    private String assigneeName;
}