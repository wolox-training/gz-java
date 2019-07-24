package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);

  @Query("select u from User u where u.name like %:name% and (u.birthday >= :startDate and u.birthday <= :endDate) or u.birthday is null")
  List<User> findByNameContainingIgnoreCaseAndBirthdayBetween(@Param("name") String name,
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}


