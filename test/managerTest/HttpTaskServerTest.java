package managerTest;

import com.google.gson.Gson;
import main.http.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.tasks.*;
import main.KVServer;
import main.manager.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    private final Gson gson = Manager.getGson();

    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private HttpClient httpClient;
    private Task task;
    private KVServer kvServer;

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = Manager.getHttpTaskManager();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpClient = HttpClient.newHttpClient();
        task = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        httpTaskServer.start();
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void whenYouReceiveTheTaskListCode200ShouldBeReturned() throws IOException, InterruptedException {
        taskManager.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void whenAddingTaskCode200ShouldBeReturned() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = "{\"name\":\"Проверка\",\"description\":\"Описание\",\"date\":\"15.12.23 12:30\",\"duration\":\"15\"}";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void whenRemoveTaskCode200ShouldBeReturned() throws IOException, InterruptedException {
        taskManager.createTask(task);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void whenYouReceiveTheTaskByIdCode200ShouldBeReturned() throws IOException, InterruptedException {
        taskManager.createTask(task);
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void whenRemoveTaskByIdCode200ShouldBeReturned() throws IOException, InterruptedException {
        taskManager.createTask(task);
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void whenReceivingEpicSubtaskCode200ShouldBeReturned() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("Epic", "java/model");
        taskManager.createEpicTask(epic);
        SubTask subTask = new SubTask("SubTask", "Description", epic.getId(),
                LocalDateTime.of(2022, 11, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask);

        URI url = URI.create("http://localhost:8080/tasks/subTask/epic/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void whenYouReceiveHistoryListCode200ShouldBeReturned() throws IOException, InterruptedException {
        Task task2 = new Task("Task2", "Description");
        List<Task> historyList = new ArrayList<>();
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task2.getId());
        historyList.add(task);
        historyList.add(task2);

        String jsonHistory = gson.toJson(historyList);

        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(jsonHistory, response.body());
    }

    @Test
    public void whenReceivingPriorityTasks200CodeShouldBeReturned() throws IOException, InterruptedException {
        TreeSet<Task> prioritizedTasks = new TreeSet<>(
                Comparator.nullsLast(Comparator.comparing(Task::getStartTime)));
        Task task2 = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task);
        taskManager.createTask(task2);
        prioritizedTasks.add(task);
        prioritizedTasks.add(task2);

        String jsonPrioritizedTasks = gson.toJson(prioritizedTasks);

        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(jsonPrioritizedTasks, response.body());
    }
}