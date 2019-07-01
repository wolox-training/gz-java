package wolox.training.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {

  @Autowired
  private BookRepository bookRepository;

  @GetMapping("/greeting")
  public String greeting(
      @RequestParam(name = "name", required = false, defaultValue = "World") String name,
      Model model) {
    model.addAttribute("name", name);
    return "greeting";
  }

  @GetMapping
  public Iterable findAll() {
    return bookRepository.findAll();
  }

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public Book create(@RequestBody Book book) {
    return bookRepository.save(book);
  }

  @GetMapping("/details/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Book findOne(@PathVariable int id) throws BookNotFoundException {
    Optional<Book> book = bookRepository.findById(id);
    if (book.isPresent()) {
      return book.get();
    }
    throw new BookNotFoundException("Book not found");
  }

  @DeleteMapping("/delete/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void delete(@PathVariable int id) throws BookNotFoundException {
    if (bookRepository.findById(id).isPresent()) {
      bookRepository.deleteById(id);
    }
    else throw new BookNotFoundException("Book not found");
  }

  @PutMapping("/update")
  public Book updateBook(@RequestBody Book book) throws BookNotFoundException {
    if (!bookRepository.findById(book.getId()).isPresent()) {
      throw new BookNotFoundException("Book not found");
    }
    return bookRepository.save(book);
  }
}