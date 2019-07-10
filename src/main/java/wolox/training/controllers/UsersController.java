package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.Users;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UsersRepository;

@RestController
@RequestMapping("/api/users")
@Api
public class UsersController {
  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  @ApiOperation(value = "Return all users.")
  @ApiResponse(code = 200, message = "Users successfully retrieved.")
  @ResponseStatus(HttpStatus.OK)
  public Iterable findAll() {
    return usersRepository.findAll();
  }

  @PostMapping
  @ApiOperation(value = "Create a new user.")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "User successfully created.")
  })
  @ResponseStatus(HttpStatus.CREATED)
  public Users create(@RequestBody Users user) {
    return usersRepository.save(user);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Giving an id, returns the user.")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "User successfully retrieved."),
      @ApiResponse(code = 404, message = "The user does not exists.")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Users findOne(@PathVariable long id) throws UserNotFoundException {
    return usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Giving an id, delete the user.")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "User successfully deleted."),
      @ApiResponse(code = 404, message = "The user does not exists.")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void delete(@PathVariable long id) throws UserNotFoundException {
    usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
    usersRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Giving an id and book info, update the user.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User successfully updated."),
      @ApiResponse(code = 400, message = "The id on the body does not match with the id received."),
      @ApiResponse(code = 404, message = "The user does not exists.")
  })
  @ResponseStatus(HttpStatus.OK)
  public Users update(@RequestBody Users user, @PathVariable long id) throws UserIdMismatchException, UserNotFoundException {
    if (user.getId() != id) {
      throw new UserIdMismatchException();
    }
    usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
    return usersRepository.save(user);
  }

  @PostMapping("/{userId}/addBook/{bookId}")
  @ApiOperation(value = "Giving a bookId and an userId, add the book to the user book collection.")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Book successfully added."),
      @ApiResponse(code = 400, message = "The book is already owned by the user."),
      @ApiResponse(code = 404, message = "The book or the user do not exists.")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Users addBook(@PathVariable long userId, @PathVariable long bookId) throws UserNotFoundException, BookAlreadyOwnedException, BookNotFoundException {
    Users user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    user.addBook(book);
    return usersRepository.save(user);
  }
}
