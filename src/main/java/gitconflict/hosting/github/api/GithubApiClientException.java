package gitconflict.hosting.github.api;

public class GithubApiClientException extends RuntimeException {
    public GithubApiClientException(String message, Exception e) {
        super(message, e);
    }
}
