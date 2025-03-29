package gitconflict.hosting.github.api.rest;

import gitconflict.hosting.github.api.GithubApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gitconflict.hosting.github.api.GithubApiClientException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link GithubApiClient} interface using the GitHub REST API.
 * This class is responsible for fetching changed files from a GitHub repository.
 */
public class GitHubRestClient implements GithubApiClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new GitHubRestClient.
     * Initializes the HTTP client and object mapper.
     */
    public GitHubRestClient() {
        this.httpClient = createHttpClient();
        this.objectMapper = new ObjectMapper();
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
     * @param owner the owner of the repository
     * @param repo the name of the repository
     * @param accessToken the access token for authentication
     * @param mergeBase the merge base commit hash
     * @param branch the name of the branch to compare
     * @return a list of filenames of the changed files
     * @throws URISyntaxException if the URI syntax is incorrect
     * @throws GithubApiClientException if an error occurs while fetching the changed files
     */
    @Override
    public List<String> fetchChangedFiles(String owner, String repo, String accessToken, String mergeBase, String branch) throws URISyntaxException {
        String url = String.format("https://api.github.com/repos/%s/%s/compare/%s...%s", owner, repo, mergeBase, branch);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/vnd.github.v3+json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode jsonNode = objectMapper.readTree(response.body());
            return jsonNode.get("files").findValues("filename").stream()
                    .map(JsonNode::asText)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new GithubApiClientException("Failed to fetch changed files from Github", e);
        }
    }
}
