package gitconflict.hosting;

import java.net.URISyntaxException;
import java.util.List;

public interface VCSHostingProvider {
    List<String> getChangedFilesSinceMergeBase(String owner, String repo, String accessToken, String mergeBase, String branch) throws URISyntaxException;
}
