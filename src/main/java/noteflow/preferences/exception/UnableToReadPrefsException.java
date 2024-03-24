package noteflow.preferences.exception;

public class UnableToReadPrefsException extends RuntimeException {
    public UnableToReadPrefsException() {
        super("The preferences could not be retrieved.");
    }
}
