package gitconflict;

import gitconflict.git.LocalGitExecutor;
import gitconflict.hosting.VCSHostingProvider;
import gitconflict.hosting.github.GitHubProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the GitConflictFinder interface.
 * Provides methods to find conflicting files in a Git repository.
 */
public class GitConflictFinderImpl implements GitConflictFinder {
    private final String owner;
    private final String repo;
    private final String accessToken;
    private final String localRepoPath;
    private final String branchA;
    private final String branchB;

    /**
     * Constructs a new GitConflictFinderImpl with the specified parameters.
     *
     * @param owner the owner of the repository
     * @param repo the name of the repository
     * @param accessToken the access token for authentication
     * @param localRepoPath the local path to the repository
     * @param branchA the name of the first branch to compare
     * @param branchB the name of the second branch to compare
     */
    public GitConflictFinderImpl(String owner, String repo, String accessToken,
                                 String localRepoPath, String branchA, String branchB) {
        this.owner = owner;
        this.repo = repo;
        this.accessToken = accessToken;
        this.localRepoPath = localRepoPath;
        this.branchA = branchA;
        this.branchB = branchB;
    }

    /**
     * Finds the list of files that have conflicts between two branches.
     *
     * @return a list of filenames of the conflicting files
     * @throws Exception if an error occurs while finding conflicting files
     */
    @Override
    public List<String> findConflictingFiles() throws Exception {
        LocalGitExecutor localGitExecutor = new LocalGitExecutor(localRepoPath);
        String mergeBase = localGitExecutor.getMergeBase(branchA, branchB);
        List<String> branchBChangedFiles = localGitExecutor.getChangedFilesSinceMergeBase(mergeBase, branchB);

        VCSHostingProvider hostingProvider = new GitHubProvider();
        List<String> branchAChangedFiles = hostingProvider.getChangedFilesSinceMergeBase(owner, repo, accessToken, mergeBase, branchA);

        Set<String> conflictingFiles = new HashSet<>(branchAChangedFiles);
        conflictingFiles.retainAll(branchBChangedFiles);

        return List.copyOf(conflictingFiles);
    }
}
