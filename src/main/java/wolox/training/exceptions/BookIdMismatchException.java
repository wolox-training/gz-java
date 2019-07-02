package wolox.training.exceptions;

public class BookIdMismatchException extends Exception {
  public BookIdMismatchException() {
    super("The received id does not match with the book id in the body.");
  }
}
