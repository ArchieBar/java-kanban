package main.manager;

import com.google.gson.Gson;
import main.http.HttpTaskManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Manager {
    public static Gson getGson() {
        return new Gson();
    }
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HttpTaskManager getHttpTaskManager() throws MalformedURLException {
        return new HttpTaskManager(8078);
    }
    public static FileBackedTasksManager getFileBackedTasksManager() throws ManagerSaveException {
        File file = new File("resources/dataMemory.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            return fileBackedTasksManager.load();
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Невозможно загрузить файл: " + file.getName());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
