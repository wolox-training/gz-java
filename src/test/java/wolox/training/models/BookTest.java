package wolox.training.models;

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
import wolox.training.repositories.BookRepository;

@AutoConfigureTestDatabase(replace=Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
public class BookTest {
  @Autowired
  private TestEntityManager bookManager;

  @Autowired
  private BookRepository bookRepository;

  @Test
  public void whenBookIsCreated_thenBookIsPersisted() {
    Book user = getBook();
    Book persistedBook = bookManager.persistAndFlush(user);
    Book foundBook = bookRepository.findById(user.getId()).get();
    assert (foundBook).equals(persistedBook);
  }

  @Test(expected = PersistenceException.class)
  public void whenCreateBookWithNullValues_thenBookIsNotPersisted() {
    Book book = new Book();
    Book persistedBook = bookManager.persistAndFlush(book);
  }

  @Test
  public void whenUpdateBook_thenBookIsUpdated() {
    Book book = getBook();
    Book persistedBook = bookManager.persistAndFlush(book);
    persistedBook.setAuthor("Eduardo Anguita - Martín Caparrós");
    bookManager.merge(persistedBook);
    Book foundBook = bookRepository.findById(book.getId()).get();
    assert ("Eduardo Anguita - Martín Caparrós").equals(foundBook.getAuthor());
  }

  @Test
  public void whenDeleteBook_thenBookIsDeleted() {
    Book book = getBook();
    bookManager.persistAndFlush(book);
    bookManager.remove(book);
    Optional<Book> foundBook = bookRepository.findById(book.getId());
    assert (!foundBook.isPresent());
  }

  private Book getBook() {
    Book book = new Book();
    book.setAuthor("Eduardo Anguita");
    book.setGenre("Historia");
    book.setImage("image.png");
    book.setIsbn("9789875800717");
    book.setPages(560);
    book.setYear("2006");
    book.setPublisher("Booket");
    book.setSubtitle("La Patria Peronista");
    book.setTitle("La Voluntad");
    return book;
  }
}
