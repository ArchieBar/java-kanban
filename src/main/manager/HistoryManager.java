package main.manager;

import main.tasks.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();

    void addHistory(Task task);

    void remove(int id);
}
