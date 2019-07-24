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
import wolox.training.repositories.UserRepository;

@AutoConfigureTestDatabase(replace=Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
public class UserTest {
  @Autowired
  private TestEntityManager userManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void whenUserIsCreated_thenUserIsPersisted() {
    User user = getUser();
    User persistedUser = userManager.persistAndFlush(user);
    User foundUser = userRepository.findById(user.getId()).get();
    assert (foundUser).equals(persistedUser);
  }

  @Test(expected = PersistenceException.class)
  public void whenCreateUserWithNullValues_thenUserIsNotPersisted() {
    User user = new User();
    User persistedUser = userManager.persistAndFlush(user);
  }

  @Test
  public void whenUpdateUser_thenUserIsUpdated() {
    User user = getUser();
    User persistedUser = userManager.persistAndFlush(user);
    persistedUser.setName("Gonzalo Matías Zamudio");
    userManager.merge(persistedUser);
    User foundUser = userRepository.findById(user.getId()).get();
    assert ("Gonzalo Matías Zamudio").equals(foundUser.getName());
  }

  @Test
  public void whenDeleteUser_thenUserIsDeleted() {
    User user = getUser();
    userManager.persistAndFlush(user);
    userManager.remove(user);
    Optional<User> foundUser = userRepository.findById(user.getId());
    assert (!foundUser.isPresent());
  }

  private User getUser() {
    User user = new User();
    user.setName("Gonzalo Zamudio");
    user.setUsername("gzamudio");
    user.setBirthday(LocalDate.parse("1990-01-09"));
    return user;
  }
}
