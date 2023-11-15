package manager;

import java.io.File;

public class Manager {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static FileBackedTasksManager getFileBackedTasksManager() {
        return FileBackedTasksManager.load(new File("dataStorage/dataMemory.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
