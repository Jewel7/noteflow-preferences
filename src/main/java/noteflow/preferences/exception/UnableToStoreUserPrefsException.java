package noteflow.preferences.exception;

public class UnableToStoreUserPrefsException extends RuntimeException {
    public UnableToStoreUserPrefsException() {
        super("The preferences could not be stored.");
    }
}
