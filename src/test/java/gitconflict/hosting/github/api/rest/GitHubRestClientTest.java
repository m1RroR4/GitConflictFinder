package gitconflict.hosting.github.api.rest;

import gitconflict.hosting.github.api.GithubApiClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GitHubRestClientTest {
    private GitHubRestClient client;
    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        client = new GitHubRestClient() {
            @Override
            HttpClient createHttpClient() {
                return httpClient;
            }
        };
    }

    @Test
    void fetchChangedFiles_usesDiffFormat() throws Exception {
        String diffResponse = "diff --git a/file1.txt b/file1.txt\nindex 123..456 789\n--- a/file1.txt\n+++ b/file1.txt";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(diffResponse);
        when(mockResponse.statusCode()).thenReturn(200);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        List<String> result = client.fetchChangedFiles("owner", "repo", "token", "abc123", "main");

        assertEquals(List.of("file1.txt"), result);
        verify(httpClient).send(argThat(request ->
                request.uri().toString().equals("https://api.github.com/repos/owner/repo/compare/abc123...main") &&
                        request.headers().firstValue("Authorization").orElse("").equals("Bearer token") &&
                        request.headers().firstValue("Accept").orElse("").equals("application/vnd.github.v3.diff")
        ), eq(HttpResponse.BodyHandlers.ofString()));
    }

    @Test
    void fetchChangedFiles_multipleFilesInDiff_returnsAllFiles() throws Exception {
        String diffResponse = "diff --git a/file1.txt b/file1.txt\nindex 123..456 789\n" +
                "diff --git a/dir/file2.txt b/dir/file2.txt\nindex 789..012 345\n" +
                "diff --git a/src/main/java/File3.java b/src/main/java/File3.java\nindex 321..654 987";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(diffResponse);
        when(mockResponse.statusCode()).thenReturn(200);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        List<String> result = client.fetchChangedFiles("owner", "repo", "token", "abc123", "main");

        assertEquals(3, result.size());
        assertTrue(result.containsAll(List.of("file1.txt", "dir/file2.txt", "src/main/java/File3.java")));
    }

    @Test
    void fetchChangedFiles_emptyDiff_returnsEmptyList() throws Exception {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn("");
        when(mockResponse.statusCode()).thenReturn(200);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        List<String> result = client.fetchChangedFiles("owner", "repo", "token", "abc123", "main");

        assertTrue(result.isEmpty());
    }

    @Test
    void fetchChangedFiles_malformedDiff_returnsEmptyList() throws Exception {
        String malformedDiff = "Some text without proper diff format\nAnother line";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(malformedDiff);
        when(mockResponse.statusCode()).thenReturn(200);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        List<String> result = client.fetchChangedFiles("owner", "repo", "token", "abc123", "main");

        assertTrue(result.isEmpty());
    }

    @Test
    void fetchChangedFiles_renamedFile_returnsOldAndNewPaths() throws Exception {
        String diffWithRename = "diff --git a/old_path.txt b/new_path.txt\nindex 123..456 789";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(diffWithRename);
        when(mockResponse.statusCode()).thenReturn(200);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        List<String> result = client.fetchChangedFiles("owner", "repo", "token", "abc123", "main");

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of("old_path.txt", "new_path.txt")));
    }

    @Test
    void fetchChangedFiles_nonOkStatusCode_throwsGithubApiClientException() throws Exception {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(404);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        assertThrows(GithubApiClientException.class, () ->
                client.fetchChangedFiles("owner", "nonexistent-repo", "token", "abc123", "main")
        );
    }

    @Test
    void fetchChangedFiles_httpError_throwsGithubApiClientException() throws Exception {
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(new IOException("Network error"));

        assertThrows(GithubApiClientException.class, () ->
                client.fetchChangedFiles("owner", "repo", "token", "abc123", "main")
        );
    }

    @Test
    void fetchChangedFiles_nullParameters_throwsIllegalArgumentException() {
        assertThrows(GithubApiClientException.class, () ->
                client.fetchChangedFiles(null, "repo", "token", "abc123", "main")
        );

        assertThrows(GithubApiClientException.class, () ->
                client.fetchChangedFiles("owner", null, "token", "abc123", "main")
        );

        assertThrows(GithubApiClientException.class, () ->
                client.fetchChangedFiles("owner", "repo", null, "abc123", "main")
        );

        assertThrows(GithubApiClientException.class, () ->
                client.fetchChangedFiles("owner", "repo", "token", null, "main")
        );

        assertThrows(GithubApiClientException.class, () ->
                client.fetchChangedFiles("owner", "repo", "token", "abc123", null)
        );
    }

    @Test
    void defaultConstructor_createsHttpClient() {
        GitHubRestClient defaultClient = new GitHubRestClient();
        assertNotNull(defaultClient);
    }
}
