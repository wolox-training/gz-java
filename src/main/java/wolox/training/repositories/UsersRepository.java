package wolox.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.models.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
  Users findByUsername(String username);
}

