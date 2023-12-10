package main.manager;

import com.google.gson.Gson;

import java.io.File;

public class Manager {
    public static Gson getGson() {
        return new Gson();
    }
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static FileBackedTasksManager getFileBackedTasksManager() throws ManagerSaveException {
        File file = new File("resources/dataMemory.csv");
        try {
            return FileBackedTasksManager.load(file);
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Невозможно загрузить файл: " + file.getName());
        }
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
