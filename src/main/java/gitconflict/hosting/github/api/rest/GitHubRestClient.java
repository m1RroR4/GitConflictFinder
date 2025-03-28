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

public class GitHubRestClient implements GithubApiClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GitHubRestClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

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
