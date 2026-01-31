package domain.exception;

public class DataNotFoundException extends RuntimeException {

  public DataNotFoundException(Throwable cause) {
    super("Data not found", cause);
  }

  public DataNotFoundException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
