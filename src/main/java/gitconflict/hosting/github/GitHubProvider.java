package gitconflict.hosting.github;

import gitconflict.hosting.VCSHostingProvider;
import gitconflict.hosting.github.api.GithubApiClient;
import gitconflict.hosting.github.api.rest.GitHubRestClient;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Implementation of the VCSHostingProvider interface for GitHub.
 * Provides methods to interact with a GitHub repository.
 */
public class GitHubProvider implements VCSHostingProvider {
    /**
     * Gets the list of files changed since the specified merge base in a GitHub repository.
     *
     * @param owner the owner of the repository
     * @param repo the name of the repository
     * @param accessToken the access token for authentication
     * @param mergeBase the merge base commit hash
     * @param branch the name of the branch to compare
     * @return a list of filenames of the changed files
     * @throws URISyntaxException if the URI syntax is incorrect
     * @throws IllegalArgumentException if any input parameter is null or empty
     */
    @Override
    public List<String> getChangedFilesSinceMergeBase(String owner, String repo, String accessToken, String mergeBase, String branch) throws URISyntaxException, IllegalArgumentException {
        if (owner == null || owner.isEmpty() || repo == null || repo.isEmpty() || accessToken == null || accessToken.isEmpty() || mergeBase == null || mergeBase.isEmpty() || branch == null || branch.isEmpty()) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        GithubApiClient githubApiClient = new GitHubRestClient();
        return githubApiClient.fetchChangedFiles(owner, repo, accessToken, mergeBase, branch);
    }
}
