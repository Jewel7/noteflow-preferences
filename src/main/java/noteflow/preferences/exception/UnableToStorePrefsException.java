package noteflow.preferences.exception;

public class UnableToStorePrefsException extends RuntimeException {
    public UnableToStorePrefsException() {
        super("The preferences could not be stored.");
    }
}
