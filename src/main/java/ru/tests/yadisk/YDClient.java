package ru.tests.yadisk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class YDClient {
    private static final String BASE_URL = "https://cloud-api.yandex.net/v1/disk";
    private final String token;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public YDClient(String token) {
        this.token = token;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    private HttpRequest.Builder requestBuilder(String path, Map<String, String> queryParams) {
        StringBuilder url = new StringBuilder(BASE_URL + path);
        if (queryParams != null && !queryParams.isEmpty()) {
            url.append("?");
            queryParams.forEach((k, v) -> url.append(k).append("=").append(v).append("&"));
            url.deleteCharAt(url.length() - 1);
        }
        return HttpRequest.newBuilder()
                .uri(URI.create(url.toString()))
                .header("Authorization", "OAuth " + token);
    }

    // PUT /resources?path=...
    public HttpResponse<String> createFolder(String path) throws IOException, InterruptedException {
        Map<String, String> params = new HashMap<>();
        params.put("path", path);
        HttpRequest request = requestBuilder("/resources", params)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // GET /resources?path=...
    public HttpResponse<String> getFolderInfo(String path) throws IOException, InterruptedException {
        Map<String, String> params = new HashMap<>();
        params.put("path", path);
        HttpRequest request = requestBuilder("/resources", params)
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // PUT /resources/publish?path=...  (исправлено с POST на PUT)
    public HttpResponse<String> publishFolder(String path) throws IOException, InterruptedException {
        Map<String, String> params = new HashMap<>();
        params.put("path", path);
        HttpRequest request = requestBuilder("/resources/publish", params)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // DELETE /resources?path=...&permanently=true
    public HttpResponse<String> deleteFolder(String path, boolean permanently) throws IOException, InterruptedException {
        Map<String, String> params = new HashMap<>();
        params.put("path", path);
        if (permanently) {
            params.put("permanently", "true");
        }
        HttpRequest request = requestBuilder("/resources", params)
                .DELETE()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // Извлечь public_url из ответа публикации
    public String getPublicUrlFromResponse(String body) throws IOException {
        JsonNode node = objectMapper.readTree(body);
        if (node.has("href")) {
            return node.get("href").asText();
        }
        return null;
    }
}