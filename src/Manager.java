import java.util.HashMap;

public class Manager {
    private int id = 0;
    private HashMap<Integer, Task> descriptionTask = new HashMap<>();
    private HashMap<Integer, EpicTask> descriptionEpicTask = new HashMap<>();
    private HashMap<Integer, SubTask> descriptionSubTask = new HashMap<>();

    private int findId() {
        this.id++;
        return id;
    }

    void createTask(Task task) {
        int id = findId();
        task.setId(id);
        task.setStatus("NEW");
        descriptionTask.put(id, task);
    }

    void createTask(EpicTask epicTask) {
        int id = findId();
        epicTask.setId(id);
        epicTask.setStatus("NEW");
        descriptionEpicTask.put(id, epicTask);
    }

    void createTask(SubTask subTask) {
        int id = findId();
        int epicId = subTask.getIdEpicTask();
        if (descriptionEpicTask.containsKey(epicId)) {
            subTask.setId(id);
            subTask.setStatus("NEW");
            descriptionSubTask.put(id, subTask);

            EpicTask epicTask = descriptionEpicTask.get(epicId);
            epicTask.getIdSubTask().add(id);
        }
    }

    void removingAllTask() {

    }

    public HashMap<Integer, Task> getDescriptionTask() {
        return descriptionTask;
    }

    public HashMap<Integer, EpicTask> getDescriptionEpicTask() {
        return descriptionEpicTask;
    }

    public HashMap<Integer, SubTask> getDescriptionSubTask() {
        return descriptionSubTask;
    }
}
