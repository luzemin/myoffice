package com.myoffice.app;

import com.myoffice.app.common.R;
import com.myoffice.app.mapper.TaskMapper;
import com.myoffice.app.model.domain.Task;
import com.myoffice.app.model.request.TaskRequest;
import com.myoffice.app.security.UserContext;
import com.myoffice.app.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserContext userContext;

    @Test
    public void testEditTaskNotFound() {
        Mockito.when(taskMapper.selectById(2))
                .thenReturn(null);
        R resultForTask2 = taskService.editTask(TaskRequest.builder().id(2).name("new name for id 2").build());
        assertNotNull(resultForTask2);
        assertEquals("task does not exist", resultForTask2.getMessage());
    }

    @Test
    public void testEditTaskNoPermission() {
        Mockito.when(userContext.getCurrentUserId())
                .thenReturn(1);
        Mockito.when(taskMapper.selectById(2))
                .thenReturn(Task.builder().id(2).owner(3).assignee(4).build());
        R resultForTask2 = taskService.editTask(TaskRequest.builder().id(2).name("new name for id 2").build());
        assertNotNull(resultForTask2);
        assertEquals("no permission to edit task", resultForTask2.getMessage());
    }

    @Test
    public void testEditTaskSuccess() {
        Mockito.when(userContext.getCurrentUserId())
                .thenReturn(1);
        Mockito.when(taskMapper.selectById(1))
                .thenReturn(Task.builder().id(1).owner(1).assignee(2).build());
        Mockito.when(taskMapper.updateById(Task.builder().id(1).name("new name for id 1").build()))
                .thenReturn(1);
        R resultForTask1 = taskService.editTask(TaskRequest.builder().id(1).name("new name for id 1").build());
        assertNotNull(resultForTask1);
        assertEquals("success", resultForTask1.getMessage());
    }
}
