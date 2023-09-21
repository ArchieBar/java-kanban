import java.util.HashMap;
import java.util.ArrayList;

public class Manager {
    private int id = 0;
    private HashMap<Integer, Task> allTask = new HashMap<>();
    private HashMap<Integer, EpicTask> allEpicTask = new HashMap<>();
    private HashMap<Integer, SubTask> allSubTask = new HashMap<>();

    // Метод по созданию уникального идентификатора.
    private int findId() {
        this.id++;
        return id;
    }

    // 3 метода с разной сигнатурой по созданию новой задачи / эпика / подзадачи.
    void createTask(Task task) {
        int thisId = findId();
        task.setId(thisId);
        task.setStatus("NEW");
        allTask.put(thisId, task);
    }

    void createTask(EpicTask epicTask) {
        int thisId = findId();
        epicTask.setId(thisId);
        epicTask.setStatus("NEW");
        allEpicTask.put(thisId, epicTask);
    }

        // Создание подзадачи, в данном методе присваивается уникальный идентификатор для подзадачи, а так же
        // присваивается идентификатор эпика, которому принадлежит подзадача и добавление идентификатора в список
        // подзадач эпики
    void createTask(SubTask subTask) {
        int thisId = findId();
        int epicId = subTask.getIdEpicTask();
        if (allEpicTask.containsKey(epicId)) {
            subTask.setId(thisId);
            subTask.setStatus("NEW");
            allSubTask.put(thisId, subTask);

            EpicTask epicTask = allEpicTask.get(epicId);
            epicTask.getIdSubTask().add(thisId);
        }
    }

    // 3 метода по удалению каждой отдельной мапы по типу задачи.
    void removingAllTask() {
        allTask.clear();
    }

        // Удаление мапы эпиков с подзадачами, т.к. подзадачи без эпика существовать не могут
    void removingAllEpicTask() {
        for (int key: allEpicTask.keySet()) {
            EpicTask epicTask = allEpicTask.get(key);
            epicTask.getIdSubTask().clear();
        }
        allEpicTask.clear();
        allSubTask.clear();
    }

    void removingAllSubTask() {
        for (int key: allEpicTask.keySet()) {
            EpicTask epicTask = allEpicTask.get(key);
            epicTask.getIdSubTask().clear();
        }
        allSubTask.clear();
    }

    public HashMap<Integer, Task> getAllTask() {
        return allTask;
    }

    public HashMap<Integer, EpicTask> getAllEpicTask() {
        return allEpicTask;
    }

    public HashMap<Integer, SubTask> getAllSubTask() {
        return allSubTask;
    }
}
