package gitconflict.hosting.github.api;

import java.net.URISyntaxException;
import java.util.List;

public interface GithubApiClient {
    List<String> fetchChangedFiles(String owner, String repo, String accessToken, String mergeBase, String branch) throws URISyntaxException;
}
