package noteflow.preferences.exception;

public class UnableToReadUserPrefsException extends RuntimeException {
    public UnableToReadUserPrefsException() {
        super("The preferences could not be retrieved.");
    }
}
