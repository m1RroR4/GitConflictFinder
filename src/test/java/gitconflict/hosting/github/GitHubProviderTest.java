package gitconflict.hosting.github;

import gitconflict.hosting.github.api.GithubApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GitHubProviderTest {
    private GitHubProvider provider;
    private GithubApiClient apiClient;

    @BeforeEach
    void setUp() {
        apiClient = mock(GithubApiClient.class);
        provider = new GitHubProvider();
    }

    @Test
    void getChangedFilesSinceMergeBase_nullOwner_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                provider.getChangedFilesSinceMergeBase(null, "repo", "token", "abc123", "main")
        );
        verifyNoInteractions(apiClient);
    }

    @Test
    void getChangedFilesSinceMergeBase_emptyRepo_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                provider.getChangedFilesSinceMergeBase("owner", "", "token", "abc123", "main")
        );
        verifyNoInteractions(apiClient);
    }

    @Test
    void getChangedFilesSinceMergeBase_nullAccessToken_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                provider.getChangedFilesSinceMergeBase("owner", "repo", null, "abc123", "main")
        );
        verifyNoInteractions(apiClient);
    }

    @Test
    void getChangedFilesSinceMergeBase_emptyMergeBase_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                provider.getChangedFilesSinceMergeBase("owner", "repo", "token", "", "main")
        );
        verifyNoInteractions(apiClient);
    }

    @Test
    void getChangedFilesSinceMergeBase_nullBranch_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                provider.getChangedFilesSinceMergeBase("owner", "repo", "token", "abc123", null)
        );
        verifyNoInteractions(apiClient);
    }

    @Test
    void getChangedFilesSinceMergeBase_apiClientThrowsException_propagatesException() throws Exception {
        when(apiClient.fetchChangedFiles("owner", "repo", "token", "abc123", "main"))
                .thenThrow(new RuntimeException("API error"));

        assertThrows(RuntimeException.class, () ->
                provider.getChangedFilesSinceMergeBase("owner", "repo", "token", "abc123", "main")
        );
    }
}
