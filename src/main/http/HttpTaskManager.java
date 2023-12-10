package main.http;

import com.google.gson.Gson;
import main.manager.FileBackedTasksManager;
import main.manager.Manager;
import main.manager.ManagerSaveException;
import main.tasks.Task;

import javax.swing.text.StyledEditorKit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson;
    private final KVTaskClient client;

    public HttpTaskManager(int port, Boolean load) {
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

    private void load() {

    }
}
