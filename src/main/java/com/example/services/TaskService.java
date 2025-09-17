package com.example.services;

import com.example.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private Long currentId = 1L;

    public TaskService() {
        tasks.add(new Task(currentId++, "Préparer les ingrédients",
                "200g de chocolat noir, 6 œufs, 1 pincée de sel, 30g de sucre", true));
        tasks.add(new Task(currentId++, "Faire fondre le chocolat",
                "Faire fondre le chocolat au bain-marie ou au micro-ondes doucement", true));
        tasks.add(new Task(currentId++, "Séparer les blancs et les jaunes",
                "Mettre les jaunes avec le chocolat fondu et réserver les blancs", false));
        tasks.add(new Task(currentId++, "Monter les blancs en neige",
                "Battre les blancs avec une pincée de sel, ajouter le sucre en fin de montage", false));
        tasks.add(new Task(currentId++, "Incorporer délicatement",
                "Mélanger les blancs au chocolat en plusieurs fois, doucement pour ne pas casser la mousse", false));
        tasks.add(new Task(currentId++, "Réfrigérer",
                "Mettre au frais au moins 3 heures avant dégustation", false));
    }

    /**
     * Gets all tasks
     * 
     * @return a list of all tasks
     */
    public List<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Gets all incomplete tasks
     * 
     * @return a list of all incomplete tasks
     */
    public List<Task> getIncompleteTasks() {
        return tasks.stream().filter(t -> !t.isCompleted()).toList();
    }

    /**
     * Gets a specific tasks by its id
     * 
     * @param id the id of the task
     * @return the task that corresponds to the given id
     */
    public Optional<Task> getTaskById(Long id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    /**
     * Adds a task to the task list
     * 
     * @param task the task to be added to the task list
     * @return the task that was added
     */
    public Task addTask(Task task) {
        task.setId(currentId++);
        tasks.add(task);
        return task;
    }

    /**
     * updates the status of a specific task
     * 
     * @param id        the id of the task to be updated
     * @param completed the status to be given to the task
     * @return the updated task
     */
    public Optional<Task> updateStatus(Long id, boolean completed) {
        return getTaskById(id).map(task -> {
            task.setCompleted(completed);
            return task;
        });
    }
}
