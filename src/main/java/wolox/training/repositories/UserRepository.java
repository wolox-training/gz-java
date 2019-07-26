package wolox.training.repositories;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
  User findByUsername(String username);

  Page<User> findByNameContainingIgnoreCaseAndBirthdayBetween(@Param("name") String name,
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
      Pageable pageable);
}


