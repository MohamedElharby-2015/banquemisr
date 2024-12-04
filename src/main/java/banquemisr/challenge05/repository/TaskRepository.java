package banquemisr.challenge05.repository;

import banquemisr.challenge05.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {
    // Search tasks by title
    List<Task> findByTitleContaining(@Param("title") String title);

    // Search tasks by status
    List<Task> findByStatus(@Param("status") String status);
    // Search tasks by title and priority
    List<Task> findByTitleContainingAndPriority(@Param("title") String title, @Param("priority") String priority);

    // Search tasks by multiple attributes
    List<Task> findByTitleContainingAndDescriptionContainingAndStatus(@Param("title") String title,
                                                                      @Param("description") String description,
                                                                      @Param("status") String status);
    // Get paginated tasks
    Page<Task> findAll(Pageable pageable);

    // Get all the tasks it's due date today
    @Query("SELECT t FROM Task t WHERE DATE(t.dueDate) = :today")
    List<Task> findTasksDueToday(@Param("today") LocalDateTime today);
}
