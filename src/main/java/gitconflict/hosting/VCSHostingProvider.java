package gitconflict.hosting;

import java.util.List;

public interface VCSHostingProvider {
    List<String> getChangedFilesSinceMergeBase(String owner, String repo, String accessToken, String mergeBase, String branch);
}
