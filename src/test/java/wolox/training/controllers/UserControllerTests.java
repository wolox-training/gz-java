package wolox.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserRepository usersRepository;
  @MockBean
  private BookRepository booksRepository;

  private User testUser;
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
    testUser = new User();
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
    List<User> userList = new Vector<User>();
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

  @Test
  public void whenCreateUser_thenUserIsCreated() throws Exception {
    String json = "{\"username\": \"gzamudio\", \"name\": \"Gonzalo Zamudio\", "
        + "\"birthday\": \"1990-01-09\"}";
    mvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated());
  }

  @Test
  public void whenCreateUserWithNullValues_thenUserIsNotCreated() throws Exception {
    mvc.perform((post("api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"username\": \"gzamudio\", \"name\": \"Gonzalo Zamudio\"}")))
        .andExpect(status().isNotFound());
    mvc.perform((post("api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\": \"Gonzalo Zamudio\", \"birthday\": \"1990-01-09\"}")))
        .andExpect(status().isNotFound());
    mvc.perform((post("api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"username\": \"gzamudio\", \"birthday\": \"1990-01-09\"}")))
        .andExpect(status().isNotFound());
  }

  @Test
  public void whenUpdateUser_thenUserIsUpdated() throws Exception {
    Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
    String json = "{\"id\": 1, \"username\": \"gzamudio\", \"name\": \"Gonzalo Matias Zamudio\", "
        + "\"birthday\": \"1990-01-09\"}";
    mvc.perform(put("/api/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk());
  }

  @Test
  public void whenUpdateUserWithIdMismatch_thenUserIsNotUpdated() throws Exception {
    Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
    String json = "{\"id\": 1, \"username\": \"gzamudio\", \"name\": \"Gonzalo Matias Zamudio\", "
        + "\"birthday\": \"1990-01-09\"}";
    mvc.perform(put("/api/users/2")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void whenUpdateUserWithInvalidId_thenUserIsNotFound() throws Exception {
    Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
    String updateJson =
        "{\"id\": 2, \"username\": \"gzamudio\", \"name\": \"Gonzalo Matias Zamudio\", "
            + "\"birthday\": \"1990-01-09\"}";
    mvc.perform(put("/api/users/2")
        .contentType(MediaType.APPLICATION_JSON)
        .content(updateJson))
        .andExpect(status().isNotFound());
  }

  @Test
  public void whenDeleteUser_thenUserIsDeleted() throws Exception {
    Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
    mvc.perform(delete("/api/users/1"))
        .andExpect(status().isAccepted());
  }

  @Test
  public void whenDeleteNonExistingUser_thenUserIsNotFound() throws Exception {
    Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
    mvc.perform(delete("/api/users/2"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void whenAddRepeatedBookToUser_thenBookIsNotAdded() throws Exception {
    Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
    Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook));
    mvc.perform(post("/api/users/1/addBook/1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void whenAddNonExistingBookToUser_thenBookIsNotAdded() throws Exception {
    Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
    mvc.perform(post("/api/users/1/addBook/3"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void whenAddBookToNonExistingUser_thenBookIsNotAdded() throws Exception {
    Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
    Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook));
    mvc.perform(post("/api/users/2/addBook/1"))
        .andExpect(status().isNotFound());
  }
}
