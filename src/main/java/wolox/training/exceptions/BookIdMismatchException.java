package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BookIdMismatchException extends Exception {
  public BookIdMismatchException() {
    super("The received id does not match with the book id in the body.");
  }
}
