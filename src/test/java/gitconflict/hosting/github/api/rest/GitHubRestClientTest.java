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
    void fetchChangedFiles_success() throws Exception {
        String jsonResponse = "{\"files\": [{\"filename\": \"file1.txt\"}, {\"filename\": \"file2.txt\"}]}";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(jsonResponse);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        List<String> result = client.fetchChangedFiles("owner", "repo", "token", "abc123", "main");

        assertEquals(List.of("file1.txt", "file2.txt"), result);
        verify(httpClient).send(argThat(request ->
                request.uri().toString().equals("https://api.github.com/repos/owner/repo/compare/abc123...main") &&
                        request.headers().firstValue("Authorization").orElse("").equals("Bearer token") &&
                        request.headers().firstValue("Accept").orElse("").equals("application/vnd.github.v3+json")
        ), eq(HttpResponse.BodyHandlers.ofString()));
    }

    @Test
    void fetchChangedFiles_invalidJson_throwsGithubApiClientException() throws Exception {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn("{invalid json}");
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        assertThrows(GithubApiClientException.class, () ->
                client.fetchChangedFiles("owner", "repo", "token", "abc123", "main")
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
}
