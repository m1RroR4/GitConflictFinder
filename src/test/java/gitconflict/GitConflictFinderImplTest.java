package gitconflict;

import gitconflict.git.LocalGitExecutor;
import gitconflict.hosting.VCSHostingProvider;
import gitconflict.hosting.github.GitHubProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GitConflictFinderImplTest {
    private LocalGitExecutor mockLocalGitExecutor;
    private VCSHostingProvider mockHostingProvider;
    private GitConflictFinderImpl gitConflictFinder;

    @BeforeEach
    void setUp() {
        mockLocalGitExecutor = mock(LocalGitExecutor.class);
        mockHostingProvider = mock(GitHubProvider.class);
        gitConflictFinder = new GitConflictFinderImpl("owner", "repo", "token", "localRepoPath", "branchA", "branchB");
    }

    @Test
    void findConflictingFiles_shouldThrowException_whenLocalGitExecutorFails() throws Exception {
        when(mockLocalGitExecutor.getMergeBase("branchA", "branchB")).thenThrow(new RuntimeException("LocalGitExecutor failed"));

        assertThrows(IOException.class, () -> gitConflictFinder.findConflictingFiles());
    }

    @Test
    void findConflictingFiles_shouldThrowException_whenHostingProviderFails() throws Exception {
        when(mockLocalGitExecutor.getMergeBase("branchA", "branchB")).thenReturn("mergeBase");
        when(mockLocalGitExecutor.getChangedFilesSinceMergeBase("mergeBase", "branchB")).thenReturn(List.of("file1.txt"));
        when(mockHostingProvider.getChangedFilesSinceMergeBase("owner", "repo", "token", "mergeBase", "branchA")).thenThrow(new RuntimeException("HostingProvider failed"));

        assertThrows(IOException.class, () -> gitConflictFinder.findConflictingFiles());
    }
}
