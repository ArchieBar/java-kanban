package main.http;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import main.manager.FileBackedTasksManager;
import main.manager.Manager;
import main.tasks.EpicTask;
import main.tasks.SubTask;
import main.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(int port, Boolean load) throws IOException, InterruptedException {
        super(null);
        gson = Manager.getGson();
        client = new KVTaskClient(port);
        if (load) {
            load();
        }
    }

    @Override
    protected void save() {
        try {
            String taskJson = gson.toJson(new ArrayList<>(allTasks.values()));
            client.put("tasks", taskJson);
            String epicTaskJson = gson.toJson(new ArrayList<>(allEpicTasks.values()));
            client.put("epicTasks", epicTaskJson);
            String subTaskJson = gson.toJson(new ArrayList<>(allSubTasks.values()));
            client.put("subTasks", subTaskJson);

            String historyJson = gson.toJson(new ArrayList<>(
                    getHistory().stream()
                            .map(Task::getId)
                            .collect(Collectors.toList())));
            client.put("history", historyJson);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void load() throws IOException, InterruptedException {
        JsonElement tasks_JE = JsonParser.parseString(client.load("tasks"));
        JsonElement subTasks_JE = JsonParser.parseString(client.load("subTasks"));
        JsonElement epicTasks_JE = JsonParser.parseString(client.load("epicTasks"));

        JsonElement history_JE = JsonParser.parseString(client.load("history"));

        if (!tasks_JE.isJsonNull() && !subTasks_JE.isJsonNull() && !epicTasks_JE.isJsonNull()) {
            JsonArray tasks_JA = tasks_JE.getAsJsonArray();
            JsonArray subTasks_JA = subTasks_JE.getAsJsonArray();
            JsonArray epicTasks_JA = epicTasks_JE.getAsJsonArray();

            for (JsonElement task : tasks_JA) {
                Task currentTask = gson.fromJson(task, Task.class);

                allTasks.put(currentTask.getId(), currentTask);
            }

            for (JsonElement subTask : subTasks_JA) {
                SubTask currentSubTask = gson.fromJson(subTask, SubTask.class);

                allSubTasks.put(currentSubTask.getId(), currentSubTask);
            }

            for (JsonElement epicTask : epicTasks_JA) {
                EpicTask currentEpictask = gson.fromJson(epicTask, EpicTask.class);

                allEpicTasks.put(currentEpictask.getId(), currentEpictask);
            }
        }

        if (!history_JE.isJsonNull()) {
            JsonArray history_JA = history_JE.getAsJsonArray();

            for (JsonElement historyId : history_JA) {
                int currentHistoryTask = gson.fromJson(historyId, Integer.class);

                historyManager.addHistory(findTask(currentHistoryTask));
            }
        }
    }
}
