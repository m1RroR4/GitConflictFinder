package gitconflict.hosting.github;

import gitconflict.hosting.HostingProvider;
import gitconflict.hosting.github.api.ApiClient;
import gitconflict.hosting.github.api.rest.RestApiClient;

import java.util.List;

public class GitHubProvider implements HostingProvider {
    @Override
    public List<String> getChangedFilesSinceMergeBase(String owner, String repo, String accessToken, String mergeBase, String branch) {
        ApiClient apiClient = new RestApiClient();
        return apiClient.fetchChangedFiles(owner, repo, accessToken, mergeBase, branch);
    }
}
