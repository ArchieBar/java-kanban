package main.http;

import main.manager.ManagerSaveException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final URL url;
    private final String apiToken;
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    public KVTaskClient(int port) throws IOException, InterruptedException {
        url = new URL("http://localhost:" + port + "/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "register"))
                .build();

        HttpResponse<String> response = client.send(request, handler);

        apiToken = response.body();
    }

    public String load(String key) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                // FIXME Добавить нормальное опсиание ошибки
                System.out.println("Что-то пошло не так... Сервер вернул код:" + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            // FIXME Добавить нормальное описание ошибки
            throw new ManagerSaveException("Что-то пошло не так...");
        }
    }

    public void put(String key, String value) throws IOException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() != 200) {
                // FIXME Добавить нормальное опсиание ошибки
                throw new ManagerSaveException("Что-то пошло не так... Сервер вернул код:" + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            // FIXME Добавить нормальное описание ошибки
            throw new ManagerSaveException("Что-то пошло не так...");
        }
    }
}
