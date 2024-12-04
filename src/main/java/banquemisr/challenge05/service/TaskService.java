package banquemisr.challenge05.service;

import banquemisr.challenge05.entities.Notification;
import banquemisr.challenge05.entities.Task;
import banquemisr.challenge05.exceptions.InvalidDueDateException;
import banquemisr.challenge05.repository.NotificationRepository;
import banquemisr.challenge05.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    TaskRepository taskRepository;

    EmailService emailService;

    NotificationRepository notificationRepository;

    public TaskService(TaskRepository taskRepository,EmailService emailService,NotificationRepository notificationRepository) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
        this.notificationRepository = notificationRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    public Task createTask(Task task) {
        if (task.getDueDate() == null) {
            throw new InvalidDueDateException("The due date cannot be null.");
        }

        if (!task.getDueDate().isAfter(LocalDateTime.now())) {
            throw new InvalidDueDateException("The due date must be in the future.");
        }

        return taskRepository.save(task);
    }
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    @Transactional
    public Task updateTask(Task updatedTask) {

        // Update fields
        updatedTask.setTitle(updatedTask.getTitle());
        updatedTask.setDescription(updatedTask.getDescription());
        updatedTask.setStatus(updatedTask.getStatus());
        updatedTask.setPriority(updatedTask.getPriority());
        updatedTask.setDueDate(updatedTask.getDueDate());

        // Update the User if the user reference is provided in the updated task
        if (updatedTask.getUser() != null) {
            updatedTask.setUser(updatedTask.getUser());
        }

        return taskRepository.save(updatedTask); // Save the updated task
    }
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // Search by title
    public List<Task> searchByTitle(String title) {
        return taskRepository.findByTitleContaining(title);
    }

    // Search by status
    public List<Task> searchByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    // Search by multiple attributes
    public List<Task> searchTasks(String title, String description, String status) {
        if (title != null && description != null && status != null) {
            return taskRepository.findByTitleContainingAndDescriptionContainingAndStatus(title, description, status);
        } else if (title != null) {
            return taskRepository.findByTitleContaining(title);
        } else if (status != null) {
            return taskRepository.findByStatus(status);
        }
        return taskRepository.findAll(); // Default to fetching all tasks if no filters
    }

    // Get paginated tasks
    public Page<Task> getPaginatedTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    // A scheduled method that runs every day on 1:00 AM.
    @Scheduled(cron = "0 0 1 * * ?")
    public void sendTaskReminders() {
        LocalDate today = LocalDate.now();

        // Fetch tasks where dueDate falls today
        List<Task> tasksDueToday = taskRepository.findAll().stream()
                .filter(task -> task.getDueDate().toLocalDate().equals(today))
                .toList();

        // Send email notifications for due tasks
        for (Task task : tasksDueToday) {
            String emailTxt = "Task : " + task.getTitle() + " - Due date is today please do the required action";
            emailService.sendEmail(task.getUser().getEmail(), task.getTitle(),emailTxt);

            // Insert this row to the notifications table
            Notification notification = new Notification(
                    "Reminder email sent for task: " + task.getTitle(),
                    emailTxt,
                    task.getUser().getEmail()
            );

            notificationRepository.save(notification);

        }
    }
}
