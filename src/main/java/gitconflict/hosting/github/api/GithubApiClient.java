package gitconflict.hosting.github.api;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Interface for a GitHub API client.
 * Provides methods to fetch changed files from a GitHub repository.
 */
public interface GithubApiClient {
    /**
     * Fetches the list of changed files between a merge base and a branch in a GitHub repository.
     *
     * @param owner the owner of the repository
     * @param repo the name of the repository
     * @param accessToken the access token for authentication
     * @param mergeBase the merge base commit hash
     * @param branch the name of the branch to compare
     * @return a list of filenames of the changed files
     * @throws URISyntaxException if the URI syntax is incorrect
     */
    List<String> fetchChangedFiles(String owner, String repo, String accessToken, String mergeBase, String branch) throws URISyntaxException;
}
