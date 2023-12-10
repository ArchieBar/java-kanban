package main.http;

import main.manager.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final String apiToken;

    public KVTaskClient(int port) {
        url = "http://localhost:" + port + "/";
        apiToken = register(url);
    }

    private String register(String url) {
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?apiToken=" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                // FIXME Добавить нормальное опсиание ошибки
                throw new ManagerSaveException("Что-то пошло не так... Сервер вернул код:" + response.statusCode());
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
                    .uri(URI.create(url + "save/" + key + "?apiToken=" + apiToken))
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
