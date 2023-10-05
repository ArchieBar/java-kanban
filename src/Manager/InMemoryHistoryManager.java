package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;
public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history = new ArrayList<>();
    private final static int MAX_COUNT_HISTORY = 10;

    @Override
    public void addHistory(Task task) {
        history.add(task);
        if (history.size() > MAX_COUNT_HISTORY) {
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}
