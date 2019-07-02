package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RestController
@RequestMapping("/api/books")
@Api
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
  @ApiOperation(value = "Return all books.")
  public Iterable findAll() {
    return bookRepository.findAll();
  }

  @PostMapping
  @ApiOperation(value = "Create a book.")
  @ResponseStatus(HttpStatus.CREATED)
  public Book create(@RequestBody Book book) {
    return bookRepository.save(book);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Giving an id, returns the book.")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Book findOne(@PathVariable long id) throws BookNotFoundException {
    return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Giving an id, delete the book.")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void delete(@PathVariable long id) throws BookNotFoundException {
    bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    bookRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Giving an id and book info, update the book.")
  public Book update(@RequestBody Book book, @PathVariable long id) throws BookNotFoundException, BookIdMismatchException {
    if (book.getId() != id) {
      throw new BookIdMismatchException();
    }
    bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    return bookRepository.save(book);
  }
}
