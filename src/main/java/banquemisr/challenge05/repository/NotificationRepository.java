package banquemisr.challenge05.repository;

import banquemisr.challenge05.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
