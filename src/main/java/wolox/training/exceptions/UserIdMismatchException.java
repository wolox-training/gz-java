package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserIdMismatchException extends Exception {
  public UserIdMismatchException() {
    super("The received id does not match with the user id in the body.");
  }
}
