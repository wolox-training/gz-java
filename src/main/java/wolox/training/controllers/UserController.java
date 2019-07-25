package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
@Api
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  private PasswordEncoder encoder = new BCryptPasswordEncoder();

  @GetMapping
  @ApiOperation(value = "Return all users.")
  @ApiResponse(code = 200, message = "Users successfully retrieved.")
  @ResponseStatus(HttpStatus.OK)
  public Iterable findAll() {
    return userRepository.findAll();
  }

  @PostMapping
  @ApiOperation(value = "Create a new user.")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "User successfully created."),
      @ApiResponse(code = 400, message = "User cannot be created. Some fields cannot be null.")
  })
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@RequestBody User user) {
    user.setPassword(encoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Giving an id, returns the user.")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "User successfully retrieved."),
      @ApiResponse(code = 404, message = "The user does not exists.")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public User findOne(@PathVariable long id) throws UserNotFoundException {
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Giving an id, delete the user.")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "User successfully deleted."),
      @ApiResponse(code = 404, message = "The user does not exists.")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void delete(@PathVariable long id) throws UserNotFoundException {
    userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    userRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Giving an id and user info, update the user.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User successfully updated."),
      @ApiResponse(code = 400, message = "The id on the body does not match with the id received."),
      @ApiResponse(code = 404, message = "The user does not exists.")
  })
  @ResponseStatus(HttpStatus.OK)
  public User update(@RequestBody User user, @PathVariable long id) throws UserIdMismatchException, UserNotFoundException {
    if (user.getId() != id) {
      throw new UserIdMismatchException();
    }
    User userToUpdate = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    userToUpdate.setName(user.getName());
    userToUpdate.setBirthday(user.getBirthday());
    userToUpdate.setUsername(user.getUsername());
    return userRepository.save(userToUpdate);
  }

  @PutMapping("/changePassword/{id}")
  @ApiOperation(value = "Giving an id and a new password, update the user.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User password successfully updated."),
      @ApiResponse(code = 400, message = "The id on the body does not match with the id received."),
      @ApiResponse(code = 404, message = "The user does not exists.")
  })
  @ResponseStatus(HttpStatus.OK)
  public User changePassword(@RequestBody User user, @PathVariable long id) throws UserIdMismatchException, UserNotFoundException {
    if (user.getId() != id) {
      throw new UserIdMismatchException();
    }
    User userToUpdate = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    userToUpdate.setPassword(encoder.encode(user.getPassword()));
    return userRepository.save(userToUpdate);
  }

  @PostMapping("/{userId}/addBook/{bookId}")
  @ApiOperation(value = "Giving a bookId and an userId, add the book to the user book collection.")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Book successfully added."),
      @ApiResponse(code = 400, message = "The book is already owned by the user."),
      @ApiResponse(code = 404, message = "The book or the user do not exists.")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public User addBook(@PathVariable long userId, @PathVariable long bookId) throws UserNotFoundException, BookAlreadyOwnedException, BookNotFoundException {
    User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    user.addBook(book);
    return userRepository.save(user);
  }

  @DeleteMapping("/{userId}/removeBook/{bookId}")
  @ApiOperation(value = "Giving a bookId and an userId, remove the book to the user book collection if exists.")
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Book successfully removed."),
      @ApiResponse(code = 404, message = "The book or the user do not exists.")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  public User removeBook(@PathVariable long userId, @PathVariable long bookId) throws UserNotFoundException, BookNotFoundException {
    User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    if (!user.getBooks().contains(book))
      throw new BookNotFoundException("The book do not exists in the user collection");
    user.removeBook(book);
    return userRepository.save(user);
  }
}
