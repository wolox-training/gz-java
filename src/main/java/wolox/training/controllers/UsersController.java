package wolox.training.controllers;

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
public class UsersController {
  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  public Iterable findAll() {
    return usersRepository.findAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Users create(@RequestBody Users user) {
    return usersRepository.save(user);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Users findOne(@PathVariable long id) throws UserNotFoundException {
    return usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void delete(@PathVariable long id) throws UserNotFoundException {
    usersRepository.findById(id).orElseThrow(UserNotFoundException::new);
    usersRepository.deleteById(id);
  }

  @PutMapping("/{id}")
  public Users update(@RequestBody Users user, @PathVariable long id) throws UserIdMismatchException {
    if (user.getId() != id) {
      throw new UserIdMismatchException();
    }
    return usersRepository.save(user);
  }

  @PostMapping("/{userId}/addBook/{bookId}")
  public Users addBook(@PathVariable long userId, @PathVariable long bookId) throws UserNotFoundException, BookAlreadyOwnedException, BookNotFoundException {
    Users user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
    user.addBook(book);
    return usersRepository.save(user);
  }
}
