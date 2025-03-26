package gitconflict.hosting.github;

import gitconflict.hosting.VCSHostingProvider;
import gitconflict.hosting.github.api.GithubApiClient;
import gitconflict.hosting.github.api.rest.GithubRestClient;

import java.net.URISyntaxException;
import java.util.List;

public class GitHubProvider implements VCSHostingProvider {
    @Override
    public List<String> getChangedFilesSinceMergeBase(String owner, String repo, String accessToken, String mergeBase, String branch) throws URISyntaxException, IllegalArgumentException {
        if (owner == null || owner.isEmpty() || repo == null || repo.isEmpty() || accessToken == null || accessToken.isEmpty() || mergeBase == null || mergeBase.isEmpty() || branch == null || branch.isEmpty()) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        GithubApiClient githubApiClient = new GithubRestClient();
        return githubApiClient.fetchChangedFiles(owner, repo, accessToken, mergeBase, branch);
    }
}
