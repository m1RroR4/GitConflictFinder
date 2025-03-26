package gitconflict;

import gitconflict.git.LocalGitExecutor;
import gitconflict.hosting.VCSHostingProvider;
import gitconflict.hosting.github.GitHubProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GitConflictFinderImpl implements GitConflictFinder {
    private final String owner;
    private final String repo;
    private final String accessToken;
    private final String localRepoPath;
    private final String branchA;
    private final String branchB;

    public GitConflictFinderImpl(String owner, String repo, String accessToken,
                                 String localRepoPath, String branchA, String branchB) {
        this.owner = owner;
        this.repo = repo;
        this.accessToken = accessToken;
        this.localRepoPath = localRepoPath;
        this.branchA = branchA;
        this.branchB = branchB;
    }


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
