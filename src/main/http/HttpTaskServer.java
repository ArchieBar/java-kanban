package main.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import main.KVServer;
import main.manager.FileBackedTasksManager;
import main.manager.Manager;
import main.manager.ManagerSaveException;
import main.manager.TaskManager;
import main.tasks.EpicTask;
import main.tasks.SubTask;
import main.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final Gson gson;
    private final HttpServer server;
    private final HttpClient client = HttpClient.newHttpClient();
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Manager.getFileBackedTasksManager());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.gson = Manager.getGson();
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.taskManager = taskManager;
        this.server.createContext("/tasks/", this::handleTasks);
    }

    private void handleTasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            String query = httpExchange.getRequestURI().getQuery();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        List<Task> listTask = taskManager.getAllTasks();
                        String listTaskJson = gson.toJson(listTask);

                        httpExchange.sendResponseHeaders(200, 0);
                        OutputStream os = httpExchange.getResponseBody();
                        os.write(listTaskJson.getBytes());
                    } else if (Pattern.matches("^/tasks/subTask/$", path)) {
                        List<SubTask> listTask = taskManager.getAllSubTasks();
                        String listTaskJson = gson.toJson(listTask);

                        httpExchange.sendResponseHeaders(200, 0);
                        OutputStream os = httpExchange.getResponseBody();
                        os.write(listTaskJson.getBytes());
                    } else if (Pattern.matches("^/tasks/epic/$", path)) {
                        List<EpicTask> listTask = taskManager.getAllEpicTasks();
                        String listTaskJson = gson.toJson(listTask);

                        httpExchange.sendResponseHeaders(200, 0);
                        OutputStream os = httpExchange.getResponseBody();
                        os.write(listTaskJson.getBytes());
                    } else if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            try {
                                Task task = taskManager.findTask(id);
                                String taskJason = gson.toJson(task);

                                httpExchange.sendResponseHeaders(200, 0);
                                OutputStream os = httpExchange.getResponseBody();
                                os.write(taskJason.getBytes());
                            } catch (NullPointerException e) {
                                e.getMessage();
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } else {
                            System.out.println("Получен некорректный id:" + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/subTask/epic/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subTask/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            List<SubTask> subTaskList = taskManager.getSubTaskOfACertainEpicTask(id);
                            // TODO добавить обработку исключения, когда не найден epic по id
                            String subtaskListJason = gson.toJson(subTaskList);

                            httpExchange.sendResponseHeaders(200, 0);
                            OutputStream os = httpExchange.getResponseBody();
                            os.write(subtaskListJason.getBytes());
                        } else {
                            System.out.println("Получен некорректный id:" + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/history/$", path)) {
                        List<Task> historyList = taskManager.getHistory();
                        String historyListJason = gson.toJson(historyList);

                        httpExchange.sendResponseHeaders(200, 0);
                        OutputStream os = httpExchange.getResponseBody();
                        os.write(historyListJason.getBytes());
                    } else if (Pattern.matches("^/tasks/$", path)) {
                        List<Task> priorityList = taskManager.getPrioritizedTasks();
                        String priorityListJason = gson.toJson(priorityList);

                        httpExchange.sendResponseHeaders(200, 0);
                        OutputStream os = httpExchange.getResponseBody();
                        os.write(priorityListJason.getBytes());
                    } else {
                        System.out.println("Получен некорректный запрос - " + path);
                        httpExchange.sendResponseHeaders(405, 0);
                    }

                    break;
                }
                case "POST": {
                    String body = readText(httpExchange);
                    JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

                    if (Pattern.matches("^/tasks/task/$", path)) {
                        // FIXME
                        //  немного не понимаю, как правильно создать задачу через метод POST,
                        //  какие конструкторы нужно предусмотреть
                        Task task = new Task(jsonObject.get("name").getAsString(),
                                jsonObject.get("description").getAsString());

                        if (taskManager.getAllTasks().contains(task)) {
                            // TODO taskManager.updateTask(newTask, oldTask);
                            System.out.println("Update");
                        } else {
                            taskManager.createTask(task);
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        System.out.println("Задача успешно создана: " + task);
                    } else if (Pattern.matches("^/tasks/subTask/$", path)) {
                        // FIXME
                        //  немного не понимаю, как правильно создать задачу через метод POST,
                        //  какие конструкторы нужно предусмотреть
                        SubTask task = new SubTask(jsonObject.get("name").getAsString(),
                                jsonObject.get("description").getAsString(), jsonObject.get("epicId").getAsInt());

                        if (taskManager.getAllSubTasks().contains(task)) {
                            // TODO taskManager.updateSubTask(newTask, oldTask);
                            System.out.println("Update");
                        } else {
                            taskManager.createSubTask(task);
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        System.out.println("Задача успешно создана: " + task);
                    } else if (Pattern.matches("^/tasks/epic/$", path)) {
                        // FIXME
                        //  немного не понимаю, как правильно создать задачу через метод POST,
                        //  какие конструкторы нужно предусмотреть
                        EpicTask task = new EpicTask(jsonObject.get("name").getAsString(),
                                jsonObject.get("description").getAsString());

                        if (taskManager.getAllEpicTasks().contains(task)) {
                            // TODO taskManager.updateEpicTask(newTask, oldTask);
                            System.out.println("Update");
                        } else {
                            taskManager.createEpicTask(task);
                        }
                        httpExchange.sendResponseHeaders(200, 0);
                        System.out.println("Задача успешно создана: " + task);
                    } else {
                        System.out.println("Получен некорректный запрос - " + path);
                        httpExchange.sendResponseHeaders(405, 0);
                    }

                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            try {
                                taskManager.deleteById(id);
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id:" + pathId);
                            httpExchange.sendResponseHeaders(405, 0);
                        }

                    } else if (Pattern.matches("^/tasks/task/$", path)) {
                        taskManager.removeAllTask();
                        httpExchange.sendResponseHeaders(200, 0);

                    } else if (Pattern.matches("^/tasks/subTask/$", path)) {
                        taskManager.removeAllSubTask();
                        httpExchange.sendResponseHeaders(200, 0);

                    } else if (Pattern.matches("^/tasks/epic/$", path)) {
                        taskManager.removeAllEpicTask();
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        System.out.println("Получен некорректный запрос - " + path +
                                " , поддерживаемые запросы метода DELETE:\n" +
                                "/tasks/task/{id} - удаление задачи по {id}.\n" +
                                "/tasks/task/ - удаление всех задач.\n" +
                                "/tasks/subTask/ - удалеине всех подзадач.\n" +
                                "/tasks/epic/ - удаление всех эпиков вместе с подзадачаами.");
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Сервер ждёт метод: GET, POST или DELETE, а получил: " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException e) {
            System.out.println("Некорректный идентификатор");
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем HttpTaskServer на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
//        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер на порту: " + PORT + " остановлен");
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
