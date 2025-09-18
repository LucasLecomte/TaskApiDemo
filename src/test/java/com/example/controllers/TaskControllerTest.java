package com.example.controllers;

import com.example.model.Task;
import com.example.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = com.example.demo.SpringTaskRestApiDemoApplication.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setup() {
        task1 = new Task(1L, "Préparer les ingrédients", "200g de chocolat noir", false);
        task2 = new Task(2L, "Faire fondre le chocolat", "Au bain-marie", false);
    }

    @Test
    void testGetAllTasks() throws Exception {
        given(taskService.getAllTasks()).willReturn(Arrays.asList(task1, task2));

        mockMvc.perform(get("/api/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("Préparer les ingrédients"))
                .andExpect(jsonPath("$[1].label").value("Faire fondre le chocolat"));
    }

    @Test
    void testGetTaskById_Found() throws Exception {
        given(taskService.getTaskById(1L)).willReturn(Optional.of(task1));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Préparer les ingrédients"));
    }

    @Test
    void testGetTaskById_NotFound() throws Exception {
        given(taskService.getTaskById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddTask() throws Exception {
        Task newTask = new Task(3L, "Déguster la mousse", "Avec une cuillère", false);
        given(taskService.addTask(any(Task.class))).willReturn(newTask);

        String json = """
                {
                  "label": "Déguster la mousse",
                  "description": "Avec une cuillère",
                  "completed": false
                }
                """;

        mockMvc.perform(post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Déguster la mousse"));
    }

    @Test
    void testUpdateStatus_Found() throws Exception {
        Task updated = new Task(1L, "Préparer les ingrédients", "200g de chocolat noir", true);
        given(taskService.updateStatus(eq(1L), eq(true))).willReturn(Optional.of(updated));

        mockMvc.perform(patch("/api/tasks/1/status?completed=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void testUpdateStatus_NotFound() throws Exception {
        given(taskService.updateStatus(eq(99L), eq(true))).willReturn(Optional.empty());

        mockMvc.perform(patch("/api/tasks/99/status?completed=true"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetIncompleteTasks_WithResults() throws Exception {
        given(taskService.getIncompleteTasks()).willReturn(Arrays.asList(task1, task2));

        mockMvc.perform(get("/api/tasks/incomplete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("Préparer les ingrédients"))
                .andExpect(jsonPath("$[1].label").value("Faire fondre le chocolat"));
    }

    @Test
    void testGetIncompleteTasks_NoResults() throws Exception {
        given(taskService.getIncompleteTasks()).willReturn(Arrays.asList());

        mockMvc.perform(get("/api/tasks/incomplete"))
                .andExpect(status().isNoContent());
    }
}
