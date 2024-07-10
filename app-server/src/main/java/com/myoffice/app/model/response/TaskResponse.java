package com.myoffice.app.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myoffice.app.model.domain.Task;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse extends Task {
    private String ownerName;
    private String assigneeNames;
}