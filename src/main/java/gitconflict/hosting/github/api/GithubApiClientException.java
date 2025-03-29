package gitconflict.hosting.github.api;

/**
 * Exception thrown when an error occurs while interacting with the GitHub API.
 */
public class GithubApiClientException extends RuntimeException {
    /**
     * Constructs a new GithubApiClientException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param e the cause of the exception
     */
    public GithubApiClientException(String message, Exception e) {
        super(message, e);
    }
}
