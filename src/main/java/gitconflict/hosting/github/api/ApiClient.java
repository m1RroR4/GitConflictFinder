package gitconflict.hosting.github.api;

import java.util.List;

public interface ApiClient {
    List<String> fetchChangedFiles(String owner, String repo, String accessToken, String mergeBase, String branch);
}
