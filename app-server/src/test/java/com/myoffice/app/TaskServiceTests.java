package com.myoffice.app;

import com.myoffice.app.common.R;
import com.myoffice.app.mapper.TaskMapper;
import com.myoffice.app.model.domain.Task;
import com.myoffice.app.model.request.TaskRequest;
import com.myoffice.app.security.user.UserContext;
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
}
