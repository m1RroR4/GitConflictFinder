package gitconflict.hosting.github.api.rest;

import gitconflict.hosting.github.api.ApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class RestApiClient implements ApiClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public RestApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<String> fetchChangedFiles(String owner, String repo, String accessToken, String mergeBase, String branch) {
        try {
            String url = String.format("https://api.github.com/repos/%s/%s/compare/%s...%s", owner, repo, mergeBase, branch);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/vnd.github.v3+json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode jsonNode = objectMapper.readTree(response.body());
            return jsonNode.get("files").findValues("filename").stream()
                    .map(JsonNode::asText)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
