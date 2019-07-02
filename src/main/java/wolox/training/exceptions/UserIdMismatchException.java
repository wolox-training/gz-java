package wolox.training.exceptions;

public class UserIdMismatchException extends Exception {
  public UserIdMismatchException() {
    super("The received id does not match with the user id in the body.");
  }
}
