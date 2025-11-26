package com.example.controllers;

import com.example.model.Task;
import com.example.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Gets all tasks
     * 
     * @return a ResponseEntity containing a list of all tasks and either HTTP code
     *         200 OK or HTTP code 204 NO CONTENT
     */
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();
        if (allTasks.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(allTasks); // 200 Ok + List<Task>
    }

    /**
     * Gets all incomplete tasks
     * 
     * @return a ResponseEntity containing a list of all incomplete tasks and either
     *         HTTP code 200 OK or HTTP code 204 NO CONTENT
     */
    @GetMapping("/incomplete")
    public ResponseEntity<List<Task>> getIncompleteTasks() {
        List<Task> incompleteTasks = taskService.getIncompleteTasks();
        if (incompleteTasks.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(incompleteTasks); // 200 Ok + List<Task>
    }

    /**
     * Gets a specific tasks by its id
     * 
     * @param id the id of the task
     * @return a ResponseEntity containing the task and either HTTP code 200 OK or
     *         HTTP code 404 NOT FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok) // 200 Ok + Task
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    /**
     * Adds a task to the task list
     * 
     * @param task the task to be added to the task list
     * @return a ResponseEntity containing the task that was added and HTTP code 200
     *         OK
     */
    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        Task created = taskService.addTask(task);
        return ResponseEntity.ok(created); // 200 Created
    }

    /**
     * updates the status of a specific task
     * 
     * @param id        the id of the task to be updated
     * @param completed the status to be given to the task
     * @return a ResponseEntity containing the updated task and either HTTP code 200
     *         OK or HTTP code 404 NOT FOUND
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id, @RequestParam boolean completed) {
        return taskService.updateStatus(id, completed)
                .map(ResponseEntity::ok) // 200 Ok + Task
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }
}
