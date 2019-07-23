package wolox.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTests {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private BookRepository booksRepository;

  private Book testBook;

  @Before
  public void setUp() {
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
  }

  @Test
  public void whenFindOne_thenBookIsReturned() throws Exception {
    Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook));
    mvc.perform(get("/api/books/1"))
        .andExpect(status().isAccepted())
        .andExpect(content().json("{\"id\": 0,"
            + "    \"genre\": \"Historia\","
            + "    \"author\": \"Eduardo Anguita\","
            + "    \"image\": \"image.png\","
            + "    \"title\": \"La Voluntad\","
            + "    \"subtitle\": \"La Patria Peronista\","
            + "    \"publisher\": \"Booket\","
            + "    \"year\": \"2006\","
            + "    \"pages\": 560,"
            + "    \"isbn\": \"9789875800717\","
            + "    \"users\": []}"));
  }

  @Test
  public void whenFindOne_thenBookIsNotFound() throws Exception {
    Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook));
    mvc.perform(get("/api/books/2"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void whenFindAll_thenBooksAreReturned() throws Exception {
    List<Book> bookList = new Vector<Book>();
    bookList.add(testBook);
    Mockito.when(booksRepository.findAll()).thenReturn(bookList);
    mvc.perform(get("/api/books/"))
        .andExpect(status().isOk())
        .andExpect(content().json("[{\"id\": 0,"
            + "    \"genre\": \"Historia\","
            + "    \"author\": \"Eduardo Anguita\","
            + "    \"image\": \"image.png\","
            + "    \"title\": \"La Voluntad\","
            + "    \"subtitle\": \"La Patria Peronista\","
            + "    \"publisher\": \"Booket\","
            + "    \"year\": \"2006\","
            + "    \"pages\": 560,"
            + "    \"isbn\": \"9789875800717\","
            + "    \"users\": []}]"));
  }

  @Test
  public void whenCreateBook_thenBookIsCreated() throws Exception {
    String json = "{\"genre\": \"Historia\", \"title\": \"La Voluntad - Tomo 4\","
        + "\"subtitle\": \"La patria peronista\", \"author\": \"Anguita - Caparrós\","
        + "\"image\": \"Imagen\", \"publisher\": \"Booket\", \"year\": \"2006\","
        + "\"pages\": 560, \"isbn\": \"9789875800717\"}";
    mvc.perform(post("/api/books/")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json))
        .andExpect(status().isCreated());
  }

  @Test
  public void whenCreateBookWithEmptyGenre_thenBookIsCreated() throws Exception {
    String json = "{\"title\": \"La Voluntad - Tomo 4\","
        + "\"subtitle\": \"La patria peronista\", \"author\": \"Anguita - Caparrós\","
        + "\"image\": \"Imagen\", \"publisher\": \"Booket\", \"year\": \"2006\","
        + "\"pages\": 560, \"isbn\": \"9789875800717\"}";
    mvc.perform(post("/api/books")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated());
  }

  @Test
  public void whenDeleteBook_thenBookIsDeleted() throws Exception {
    Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook));
    mvc.perform(delete("/api/books/1"))
        .andExpect(status().isAccepted());
  }

  @Test
  public void whenDeleteNonExistingBook_thenBookIsNotFound() throws Exception {
    Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook));
    mvc.perform(delete("/api/books/2"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void whenUpdateBook_thenBookIsUpdated() throws Exception {
    Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook));
    String json = "{ \"id\": 1, \"title\": \"La Voluntad - Tomo 4\","
        + "\"subtitle\": \"La patria peronista\", \"author\": \"Anguita - Caparrós\","
        + "\"image\": \"Imagen\", \"publisher\": \"Booket\", \"year\": \"2006\","
        + "\"pages\": 560, \"isbn\": \"9789875800717\"}";
    mvc.perform(put("/api/books/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json))
        .andExpect(status().isOk());
  }

  @Test
  public void whenUpdateNonExistingBook_thenBookIsNotFound() throws Exception {
    Mockito.when(booksRepository.findById(1L)).thenReturn(Optional.of(testBook));
    String json = "{ \"id\": 2, \"title\": \"La Voluntad - Tomo 4\","
        + "\"subtitle\": \"La patria peronista\", \"author\": \"Anguita - Caparrós\","
        + "\"image\": \"Imagen\", \"publisher\": \"Booket\", \"year\": \"2006\","
        + "\"pages\": 560, \"isbn\": \"9789875800717\"}";
    mvc.perform(put("/api/books/2")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isNotFound());
  }

  @Test
  public void whenUpdateBookWithIdMismatch_thenBookIsNotUpdated() throws Exception {
    String json = "{ \"id\": 3, \"title\": \"La Voluntad - Tomo 4\","
        + "\"subtitle\": \"La patria peronista\", \"author\": \"Anguita - Caparrós\","
        + "\"image\": \"Imagen\", \"publisher\": \"Booket\", \"year\": \"2006\","
        + "\"pages\": 560, \"isbn\": \"9789875800717\"}";
    mvc.perform(put("/api/books/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest());
  }
}
