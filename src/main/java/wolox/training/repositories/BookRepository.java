package wolox.training.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
  Book findByAuthor(String author);
  Book findByIsbn(String isbn);
  List<Book> findByGenreAndPublisherAndYear(String genre, String publisher, String year);
}
