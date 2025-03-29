package gitconflict.git;

/**
 * Exception thrown when a Git operation fails.
 */
public class GitOperationException extends RuntimeException {
    /**
     * Constructs a new GitOperationException with the specified detail message.
     *
     * @param message the detail message
     */
    public GitOperationException(String message) {
        super(message);
    }
}
