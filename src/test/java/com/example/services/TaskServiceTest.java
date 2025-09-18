package com.example.services;

import com.example.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void testGetAllTasks_NotEmpty() {
        List<Task> tasks = taskService.getAllTasks();
        assertNotNull(tasks, "The task list should not be null");
        assertFalse(tasks.isEmpty(), "The task list should not be empty at startup");
    }

    @Test
    void testAddTask() {
        Task newTask = taskService.addTask(new Task(null, "Déguster la mousse", "Avec une cuillère", false));

        assertNotNull(newTask.getId(), "The task id should be generated");
        assertEquals("Déguster la mousse", newTask.getLabel(), "The task label should be generated");
        assertEquals("Avec une cuillère", newTask.getDescription(), "The task description should be generated");
        assertFalse(newTask.getCompleted(), "The task should be incomplete by default");
        assertTrue(taskService.getAllTasks().contains(newTask), "The new task should be added to the list");
    }

    @Test
    void testGetTaskById_Found() {
        Task firstTask = taskService.getAllTasks().get(0);
        Optional<Task> found = taskService.getTaskById(firstTask.getId());

        assertTrue(found.isPresent(), "The task should be found by its ID");
        assertEquals(firstTask.getLabel(), found.get().getLabel(), "The label should match the expected task's label");
    }

    @Test
    void testGetTaskById_NotFound() {
        Optional<Task> found = taskService.getTaskById(999L);
        assertFalse(found.isPresent(), "No task should be found when searching with a non-existing ID");
    }

    @Test
    void testUpdateStatus_Found() {
        Task firstTask = taskService.getAllTasks().get(0);
        boolean initialStatus = firstTask.isCompleted();

        Optional<Task> updated = taskService.updateStatus(firstTask.getId(), !initialStatus);

        assertTrue(updated.isPresent(), "The task should exist and be updated");
        assertEquals(!initialStatus, updated.get().isCompleted(), "The status should be toggled correctly");
    }

    @Test
    void testUpdateStatus_NotFound() {
        Optional<Task> updated = taskService.updateStatus(999L, true);
        assertTrue(updated.isEmpty(), "No task should be updated if the ID does not exist");
    }

    @Test
    void testGetIncompleteTasks() {
        // Ensure there are incomplete tasks at startup
        List<Task> incomplete = taskService.getIncompleteTasks();
        assertFalse(incomplete.isEmpty(), "There should be at least one incomplete task initially");

        // Mark all tasks as completed
        taskService.getAllTasks().forEach(t -> taskService.updateStatus(t.getId(), true));

        // Ensure there are no incomplete tasks
        List<Task> empty = taskService.getIncompleteTasks();
        assertTrue(empty.isEmpty(), "When all tasks are completed, the list of incomplete tasks should be empty");
    }
}
