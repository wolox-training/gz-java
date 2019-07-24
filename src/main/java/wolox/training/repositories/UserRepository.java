package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
  List<User> findByNameContainingIgnoreCaseAndBirthdayBetween(String name, LocalDate startDate, LocalDate endDate);
}
