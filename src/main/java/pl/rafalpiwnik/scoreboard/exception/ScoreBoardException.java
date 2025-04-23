package pl.rafalpiwnik.scoreboard.exception;

public class ScoreBoardException extends RuntimeException {

    public ScoreBoardException(String message) {
        super(message);
    }

    public ScoreBoardException(String message, Throwable cause) {
        super(message, cause);
    }
}
