package gitconflict.hosting;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Interface for a Version Control System (VCS) hosting provider.
 * Provides methods to interact with a VCS repository.
 */
public interface VCSHostingProvider {
    /**
     * Gets the list of files changed since the specified merge base in a repository.
     *
     * @param owner the owner of the repository
     * @param repo the name of the repository
     * @param accessToken the access token for authentication
     * @param mergeBase the merge base commit hash
     * @param branch the name of the branch to compare
     * @return a list of filenames of the changed files
     * @throws URISyntaxException if the URI syntax is incorrect
     */
    List<String> getChangedFilesSinceMergeBase(String owner, String repo, String accessToken, String mergeBase, String branch) throws URISyntaxException;
}
