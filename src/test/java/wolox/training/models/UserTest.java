package wolox.training.models;

import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.PersistenceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.UsersRepository;

@AutoConfigureTestDatabase(replace=Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
public class UserTest {
  @Autowired
  private TestEntityManager userManager;

  @Autowired
  private UsersRepository userRepository;

  @Test
  public void whenUserIsCreated_thenUserIsPersisted() {
    Users user = getUser();
    Users persistedUser = userManager.persistAndFlush(user);
    Users foundUser = userRepository.findById(user.getId()).get();
    assert (foundUser).equals(persistedUser);
  }

  @Test(expected = PersistenceException.class)
  public void whenCreateUserWithNullValues_thenUserIsNotPersisted() {
    Users user = new Users();
    Users persistedUser = userManager.persistAndFlush(user);
  }

  @Test
  public void whenUpdateUser_thenUserIsUpdated() {
    Users user = getUser();
    Users persistedUser = userManager.persistAndFlush(user);
    persistedUser.setName("Gonzalo Matías Zamudio");
    userManager.merge(persistedUser);
    Users foundUser = userRepository.findById(user.getId()).get();
    assert ("Gonzalo Matías Zamudio").equals(foundUser.getName());
  }

  @Test
  public void whenDeleteUser_thenUserIsDeleted() {
    Users user = getUser();
    userManager.persistAndFlush(user);
    userManager.remove(user);
    Optional<Users> foundUser = userRepository.findById(user.getId());
    assert (!foundUser.isPresent());
  }

  private Users getUser() {
    Users user = new Users();
    user.setName("Gonzalo Zamudio");
    user.setUsername("gzamudio");
    user.setBirthday(LocalDate.parse("1990-01-09"));
    return user;
  }
}
