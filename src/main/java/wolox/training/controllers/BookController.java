package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import wolox.training.services.OpenLibraryService;

@RestController
@RequestMapping("/api/books")
@Api
public class BookController {

  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  @ApiOperation(value = "Return all books.")
  @ApiResponse(code = 200, message = "Books successfully retrieved.")
  @ResponseStatus(HttpStatus.OK)
  public Page<Book> findAll(Pageable pageable) {
    return bookRepository.findAll(pageable);
  }

  @PostMapping
  @ApiOperation(value = "Create a book.")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Book successfully created.")
  })
  @ResponseStatus(HttpStatus.CREATED)
  public Book create(@RequestBody Book book) {
    return bookRepository.save(book);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Giving an id, returns the book.")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Book successfully retrieved."),
      @ApiResponse(code = 404, message = "The book does not exists.")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Book findOne(@PathVariable long id) throws BookNotFoundException {
    return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Giving an id, delete the book.")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Book successfully deleted."),
      @ApiResponse(code = 404, message = "The book does not exists.")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void delete(@PathVariable long id) throws BookNotFoundException {
    bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    bookRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Giving an id and book info, update the book.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Book successfully updated."),
      @ApiResponse(code = 400, message = "The id on the body does not match with the id received."),
      @ApiResponse(code = 404, message = "The book does not exists.")
  })
  @ResponseStatus(HttpStatus.OK)
  public Book update(@RequestBody Book book, @PathVariable long id) throws BookNotFoundException, BookIdMismatchException {
    if (book.getId() != id) {
      throw new BookIdMismatchException();
    }
    bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    return bookRepository.save(book);
  }

  @GetMapping("/findByIsbn/{isbn}")
  @ApiOperation(value = "Giving an isbn, returns the related book.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Book successfully retrieved"),
      @ApiResponse(code = 201, message = "The book does not exists on DB, but it was created with OpenLibraryService information"),
      @ApiResponse(code = 400, message = "The book exists on OpenLibrary service but some not nullable fields are missing"),
      @ApiResponse(code = 404, message = "The book does not exists.")
  })
  public ResponseEntity<Book> findByIsbn(@PathVariable String isbn) {
    Book book = bookRepository.findByIsbn(isbn);
    if (book == null) {
      book = OpenLibraryService.bookInfo(isbn);
      if (book == null) {
        return new ResponseEntity("{ message: \"There is no book with the received isbn\"}" , HttpStatus.NOT_FOUND);
      }
      try {
        bookRepository.save(book);
        return new ResponseEntity(book, HttpStatus.CREATED);
      }
      catch (DataIntegrityViolationException e) {
        return new ResponseEntity("{ message: \"The book exists on OpenLibrary service but some not nullable fields are missing.\"}", HttpStatus.BAD_REQUEST);
      }
    }
    return new ResponseEntity(book, HttpStatus.OK);
  }

  @GetMapping("find/{genre}/{publisher}/{year}")
  @ApiOperation(value = "Giving a genre, publisher, year, returns the related books")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Books successfully retrieved")
  })
  @ResponseStatus(HttpStatus.OK)
  public Page<Book> find(@PathVariable String genre, @PathVariable String publisher,
      @PathVariable String year, Pageable pageable) {
    return bookRepository.findByGenreAndPublisherAndYear(genre, publisher, year, pageable);
  }

  @GetMapping("getAll")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Books successfully retrieved")
  })
  public Page<Book> getAll(@RequestParam(required = false, defaultValue = "") String genre,
    @RequestParam(required = false, defaultValue = "") String author,
    @RequestParam(required = false, defaultValue = "") String image,
    @RequestParam(required = false, defaultValue = "") String title,
    @RequestParam(required = false, defaultValue = "") String subtitle,
    @RequestParam(required = false, defaultValue = "") String publisher,
    @RequestParam(required = false, defaultValue = "") String year,
    @RequestParam(required = false, defaultValue = "") String isbn,
    @RequestParam(required = false, defaultValue = "0") int pages,
      Pageable pageable) {
    return bookRepository.getAll(genre, author, image, title, subtitle, publisher, year, isbn, pages, pageable);
  }
}
