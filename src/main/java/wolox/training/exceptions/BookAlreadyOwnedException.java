package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BookAlreadyOwnedException extends Exception {
  public BookAlreadyOwnedException() {
    super("Book already owned by this user.");
  }
}
