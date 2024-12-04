package banquemisr.challenge05.controller;

import banquemisr.challenge05.entities.Task;
import banquemisr.challenge05.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Crud operations endpoints
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();

        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content if no tasks found
        }

        return new ResponseEntity<>(tasks, HttpStatus.OK); // 200 OK with the list of tasks
    }
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Optional<Task> taskOptional = taskService.getTaskById(id);

        if (!taskOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 if the task is not found
        }

        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content for successful deletion
    }
    @PutMapping
    public ResponseEntity<Task> updateTask(@RequestBody Task updatedTask) {
        Optional<Task> taskOptional = taskService.getTaskById(updatedTask.getId());

        if (!taskOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 if the task is not found
        }

        Task task = taskService.updateTask(updatedTask);
        return ResponseEntity.ok(task); // Return the updated task
    }

    // Search tasks by title
    @GetMapping("/searchByTitle")
    public ResponseEntity<List<Task>> searchByTitle(@RequestParam("title") String title) {
        List<Task> tasks = taskService.searchByTitle(title);
        return ResponseEntity.ok(tasks);
    }

    // Search tasks by status
    @GetMapping("/searchByStatus")
    public ResponseEntity<List<Task>> searchByStatus(@RequestParam("status") String status) {
        List<Task> tasks = taskService.searchByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    // Search tasks by multiple attributes
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam(value = "title", required = false) String title,
                                                  @RequestParam(value = "description", required = false) String description,
                                                  @RequestParam(value = "status", required = false) String status) {
        List<Task> tasks = taskService.searchTasks(title, description, status);
        return ResponseEntity.ok(tasks);
    }

    // Paginated,Sorted endpoint for all tasks
    // We can make paginated result based on some filtration on any attribute as well, For Example 'Status'
    // Only ROLE_ADMIN users can call this end-point - Also we can do the same from our configuration
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/paginated")
    public ResponseEntity<Page<Task>> getPaginatedTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Task> tasks = taskService.getPaginatedTasks(pageRequest);
        return ResponseEntity.ok(tasks);
    }

}
