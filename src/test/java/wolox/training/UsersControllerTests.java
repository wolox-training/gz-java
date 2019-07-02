package wolox.training;

import java.time.LocalDate;
import java.util.Optional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.models.Book;
import wolox.training.models.Users;
import wolox.training.repositories.UsersRepository;

@DataJpaTest
public class UsersControllerTests {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private UsersRepository usersRepository;
  private Users testUser;
  private Book testBook;

  @Before(value = "")
  public void setUp() throws BookAlreadyOwnedException {
    testBook = new Book();
    testBook.setAuthor("Eduardo Anguita");
    testBook.setGenre("Historia");
    testBook.setImage("image.png");
    testBook.setIsbn("9789875800717");
    testBook.setPages(560);
    testBook.setYear("2006");
    testBook.setPublisher("Booket");
    testBook.setSubtitle("La Patria Peronista");
    testBook.setTitle("La Voluntad. Una historia de la militancia revolucionaria en la Argentina");
    testUser = new Users();
    testUser.setUsername("gzamudio");
    testUser.setName("Gonzalo Zamudio");
    testUser.setBirthday(LocalDate.parse("1990-01-09"));
    testUser.addBook(testBook);
  }

  @Test
  public void whenCreateUser_thenUserIsPersisted() {
    // Mockito.when(usersRepository.findById(1L)).thenReturn(testUser);

  }
}
