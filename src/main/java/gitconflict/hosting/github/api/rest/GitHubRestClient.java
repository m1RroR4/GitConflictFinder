package gitconflict.hosting.github.api.rest;

import gitconflict.hosting.github.api.GithubApiClient;
import gitconflict.hosting.github.api.GithubApiClientException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the {@link GithubApiClient} interface using the GitHub REST API.
 * This class is responsible for fetching changed files from a GitHub repository.
 */
public class GitHubRestClient implements GithubApiClient {
    private final HttpClient httpClient;

    /**
     * Initializes a new GitHubRestClient with default HTTP client.
     */
    public GitHubRestClient() {
        this.httpClient = createHttpClient();
    }

    /**
     * Creates a new HTTP client.
     *
     * @return a new instance of {@link HttpClient}
     */
    HttpClient createHttpClient() {
        return HttpClient.newHttpClient();
    }

    /**
     * Fetches the list of changed files between a merge base and a branch in a GitHub repository.
     *
     * @param owner       the owner of the repository
     * @param repo        the name of the repository
     * @param accessToken the access token for authentication
     * @param mergeBase   the merge base commit hash
     * @param branch      the name of the branch to compare
     * @return a list of filenames of the changed files
     * @throws URISyntaxException       if the URI syntax is incorrect
     * @throws GithubApiClientException if an error occurs while fetching the changed files
     */
    @Override
    public List<String> fetchChangedFiles(String owner, String repo, String accessToken, String mergeBase, String branch) throws URISyntaxException {
        String url = String.format("https://api.github.com/repos/%s/%s/compare/%s...%s", owner, repo, mergeBase, branch);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/vnd.github.v3.diff")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return parseChangedFilesFromDiff(response.body());
        } catch (Exception e) {
            throw new GithubApiClientException("Failed to fetch changed files from Github using diff format", e);
        }
    }

    /**
     * Parses the raw diff text and extracts the filenames of changed files.
     *
     * @param diff the raw diff text
     * @return list of changed file paths
     */
    private List<String> parseChangedFilesFromDiff(String diff) {
        Set<String> changedFiles = new HashSet<>();

        for (String line : diff.lines().toList()) {
            if (line.startsWith("diff --git")) {
                String[] parts = line.split(" ");
                if (parts.length >= 4) {
                    String aPath = parts[2].substring(2);
                    String bPath = parts[3].substring(2);
                    changedFiles.add(aPath);
                    changedFiles.add(bPath);
                }
            }
        }

        return new ArrayList<>(changedFiles);
    }
}
