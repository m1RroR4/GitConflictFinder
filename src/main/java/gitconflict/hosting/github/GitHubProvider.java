package gitconflict.hosting.github;

import gitconflict.hosting.VCSHostingProvider;
import gitconflict.hosting.github.api.GithubApiClient;
import gitconflict.hosting.github.api.rest.GithubRestClient;

import java.util.List;

public class GitHubProvider implements VCSHostingProvider {
    @Override
    public List<String> getChangedFilesSinceMergeBase(String owner, String repo, String accessToken, String mergeBase, String branch) {
        GithubApiClient githubApiClient = new GithubRestClient();
        return githubApiClient.fetchChangedFiles(owner, repo, accessToken, mergeBase, branch);
    }
}
