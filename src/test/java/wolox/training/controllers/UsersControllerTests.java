package wolox.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.Users;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UsersRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
public class UsersControllerTests {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private UsersRepository usersRepository;
  @MockBean
  private BookRepository booksRepository;

  private Users testUser;
  private Book testBook;

  @Before
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
    testBook.setTitle("La Voluntad");
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
        .andExpect(status().isAccepted())
        .andExpect(content().json(
            "{\"id\": 0, \"username\": \"gzamudio\", \"name\": \"Gonzalo Zamudio\", "
                + "\"birthday\": \"1990-01-09\", \"books\": [{\"id\": 0, \"genre\": \"Historia\", "
                + "\"author\": \"Eduardo Anguita\", \"image\": \"image.png\","
                + "\"title\": \"La Voluntad\","
                + "\"subtitle\":\"La Patria Peronista\", \"publisher\": \"Booket\", \"year\": \"2006\","
                + "\"pages\": 560, \"isbn\": \"9789875800717\"}]}"
        ));
  }

  @Test
  public void whenFindOne_thenUserIsNotFound() throws Exception {
    Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
    mvc.perform(get("/api/users/2")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void whenFindAll_thenUsersAreReturned() throws Exception {
    List<Users> userList = new Vector<Users>();
    userList.add(testUser);
    Mockito.when(usersRepository.findAll()).thenReturn(userList);
    mvc.perform(get("/api/users")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            "[{\"id\": 0, \"username\": \"gzamudio\", \"name\": \"Gonzalo Zamudio\", "
                + "\"birthday\": \"1990-01-09\", \"books\": [{\"id\": 0, \"genre\": \"Historia\", "
                + "\"author\": \"Eduardo Anguita\", \"image\": \"image.png\","
                + "\"title\": \"La Voluntad\","
                + "\"subtitle\":\"La Patria Peronista\", \"publisher\": \"Booket\", \"year\": \"2006\","
                + "\"pages\": 560, \"isbn\": \"9789875800717\"}]}]"
        ));
  }
}
