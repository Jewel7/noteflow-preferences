package noteflow.preferences.exception;

public class InvalidUserPrefsException extends RuntimeException {

    public InvalidUserPrefsException() {
        super("The provided user preferences are invalid.");
    }
}
