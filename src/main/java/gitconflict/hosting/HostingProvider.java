package gitconflict.hosting;

import java.util.List;

public interface HostingProvider {
    List<String> getChangedFilesSinceMergeBase(String owner, String repo, String accessToken, String mergeBase, String branch);
}
