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

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public Users create(@RequestBody Users user) {
    return usersRepository.save(user);
  }

  @GetMapping("/details/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Users findOne(@PathVariable int id) throws UserNotFoundException {
    Optional<Users> user = usersRepository.findById(id);
    if (user.isPresent()) {
      return user.get();
    }
    throw new UserNotFoundException("User not found");
  }

  @DeleteMapping("/delete/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void delete(@PathVariable int id) throws UserNotFoundException {
    if (usersRepository.findById(id).isPresent()) {
      usersRepository.deleteById(id);
    }
    else throw new UserNotFoundException("User not found");
  }

  @PutMapping("/update")
  public Users updateBook(@RequestBody Users user) throws UserNotFoundException {
    if (!usersRepository.findById(user.getId()).isPresent()) {
      throw new UserNotFoundException("User not found");
    }
    return usersRepository.save(user);
  }

  @PostMapping("/{userId}/addBook/{bookId}")
  public Users addBook(@PathVariable int userId, @PathVariable int bookId) throws UserNotFoundException, BookAlreadyOwnedException, BookNotFoundException {
    Optional<Users> user = usersRepository.findById(userId);
    if (!user.isPresent())
      throw new UserNotFoundException("User not found");
    Optional<Book> book = bookRepository.findById(bookId);
    if (!book.isPresent())
      throw new BookNotFoundException("Book not found");
    user.get().addBook(book.get());
    usersRepository.save(user.get());
    return user.get();
  }
}
