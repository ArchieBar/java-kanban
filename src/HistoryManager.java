import java.util.ArrayList;

public interface HistoryManager {
    public ArrayList<Task> getHistory();

    public void addHistory(Task task);
}
