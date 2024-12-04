package banquemisr.challenge05.exceptions;

public class InvalidDueDateException extends RuntimeException {
    public InvalidDueDateException(String message) {
        super(message);
    }
}