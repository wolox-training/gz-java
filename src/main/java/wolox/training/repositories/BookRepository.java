package wolox.training.repositories;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import wolox.training.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
  Book findByAuthor(String author);
  Book findByIsbn(String isbn);

  @Query("select b from Book b where (b.genre = :genre or b.genre is null) and "
      + "(b.publisher = :publisher or b.publisher is null) and "
      + "(b.year = :year or b.year is null)")
  List<Book> findByGenreAndPublisherAndYear(@Param("genre") String genre,
      @Param("publisher") String publisher, @Param("year") String year);

  List<Book> getAll(@RequestParam Map<String,String> allParams);
}
