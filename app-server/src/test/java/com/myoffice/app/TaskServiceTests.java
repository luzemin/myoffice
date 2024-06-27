package com.myoffice.app;

import com.myoffice.app.common.R;
import com.myoffice.app.mapper.TaskMapper;
import com.myoffice.app.model.domain.Task;
import com.myoffice.app.model.domain.User;
import com.myoffice.app.model.request.TaskRequest;
import com.myoffice.app.service.UserService;
import com.myoffice.app.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TaskServiceTests {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserService userService;

    @BeforeEach
    public void before() {
        //set current login user as user(id=1)
        Mockito.when(userService.getCurrentUser())
                .thenReturn(User.builder().id(1).build());
    }

    @Test
    public void testEditTaskSuccess() {

        Mockito.when(taskMapper.selectById(1))
                .thenReturn(Task.builder().id(1).owner(1).assignee(2).build());
        Mockito.when(taskMapper.updateById(Task.builder().id(1).name("new name for id 1").build()))
                .thenReturn(1);
        R resultForTask1 = taskService.editTask(TaskRequest.builder().id(1).name("new name for id 1").build());
        assertNotNull(resultForTask1);
        assertEquals("success", resultForTask1.getMessage());
    }

    @Test
    public void testEditTaskNoPermission() {
        //set task2 owner is 3 and assign is 4
        Mockito.when(taskMapper.selectById(2))
                .thenReturn(Task.builder().id(2).owner(3).assignee(4).build());

        Mockito.when(taskMapper.updateById(Task.builder().id(2).name("new name for id 2").build()))
                .thenReturn(1);

        R resultForTask2 = taskService.editTask(TaskRequest.builder().id(2).name("new name for id 2").build());
        assertNotNull(resultForTask2);
        assertEquals("no permission to edit task", resultForTask2.getMessage());
    }

    @Test
    public void testEditTaskNotFound() {
        //set task2 not found
        Mockito.when(taskMapper.selectById(2))
                .thenReturn(null);

        Mockito.when(taskMapper.updateById(Task.builder().id(2).name("new name for id 2").build()))
                .thenReturn(1);

        R resultForTask2 = taskService.editTask(TaskRequest.builder().id(2).name("new name for id 2").build());
        assertNotNull(resultForTask2);
        assertEquals("task does not exist", resultForTask2.getMessage());
    }
}