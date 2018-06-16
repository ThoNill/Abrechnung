package ddd;

public class AssertionException extends RuntimeException {

    public AssertionException() {
        super();
    }

    public AssertionException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AssertionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssertionException(String message) {
        super(message);
    }

    public AssertionException(Throwable cause) {
        super(cause);
    }

}
