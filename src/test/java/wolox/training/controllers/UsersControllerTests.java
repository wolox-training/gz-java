package wolox.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.models.Book;
import wolox.training.models.Users;
import wolox.training.repositories.UsersRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
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
  public void whenFindOne_thenUserIsReturned() throws Exception {
    Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
    mvc.perform(get("/api/users/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "{\"id\": 0, \"username\": \"gzamudio\", \"name\": \"Gonzalo Zamudio\", \"birthday\": \"1990-01-09\" }"
        ));
  }
}
