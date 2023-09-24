import java.util.ArrayList;
import java.util.HashMap;

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
    // подзадач эпика
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

<<<<<<< HEAD
    // 3 метода по обновлению задач для каждого из типов.
    void updateTask(Task newTask, String newStatus, int thisId) {
        newTask.setStatus(newStatus);
        allTask.put(thisId, newTask);
    }

<<<<<<< HEAD
    // Обновление статуса эпика построено на сравнении всех элементов массива с первым
    // Если все они равны первому, то присвоить эпику статус первой подзадачи в массиве
    // Если нет, то присвоить эпику статут: "IN_PROGRESS".
    void updateEpicTask(EpicTask newEpicTask, int thisId) {
        EpicTask epicTask = allEpicTask.get(thisId);
        String intermediateStatus;

        if (!(epicTask.getIdSubTask() == null)) {
            intermediateStatus = allSubTask.get(epicTask.getIdSubTask().get(0)).getStatus();
            for (Integer idSubTask : epicTask.getIdSubTask()) {
                if (!(intermediateStatus == allSubTask.get(idSubTask).getStatus())) {
                    intermediateStatus = "IN_PROGRESS";
                }
            }
        } else {
            intermediateStatus = "NEW";
        }

        epicTask.setStatus(intermediateStatus);
        allEpicTask.put(thisId, newEpicTask);
    }

    void updateSubTask(SubTask newSubTask, String newStatus, int thisId) {
        int idEpicTask = allSubTask.get(thisId).getIdEpicTask();
        newSubTask.setStatus(newStatus);
        allSubTask.put(thisId, newSubTask);
        updateEpicTask(allEpicTask.get(idEpicTask), idEpicTask);
    }

    // 3 метода по удалению каждой отдельной мапы по типу задачи.
    void removeAllTask() {
        allTask.clear();
    }

    // Удаление мапы эпиков с подзадачами, т.к. подзадачи без эпика существовать не могут.
    void removeAllEpicTask() {
        for (int key : allEpicTask.keySet()) {
            EpicTask epicTask = allEpicTask.get(key);
            epicTask.getIdSubTask().clear();
        }
        allEpicTask.clear();
        allSubTask.clear();
    }

    void removeAllSubTask() {
        for (int idEpicTask : allEpicTask.keySet()) {
            EpicTask epicTask = allEpicTask.get(idEpicTask);
            epicTask.getIdSubTask().clear();
            epicTask.setStatus("NEW");
        }
        allSubTask.clear();
    }

    // 3 метода по удалению задач по их идентификатору.
    void removeTaskById(int idTask) {
        allTask.remove(idTask);
    }

    // Удаление отдельного эпика, а также удаление связанных с ним подзадач.
    void removeEpicTaskById(int idEpicTask) {
        EpicTask epicTask = allEpicTask.get(idEpicTask);
        for (Integer id : epicTask.getIdSubTask()) {
            allSubTask.remove(id);
        }
        epicTask.getIdSubTask().clear();
        allEpicTask.remove(idEpicTask);
    }

    // Удаление отдельной подзадачи, а так же редактирование списка 'idSubTask' у эпика
    void removeSubTaskById(int idSubTask) {
        int idEpicTask = allSubTask.get(idSubTask).getIdEpicTask();
        EpicTask epicTask = allEpicTask.get(idEpicTask);
        int idSubTaskInEpicTask = epicTask.getIdSubTask().indexOf(idSubTask);
        epicTask.getIdSubTask().remove(idSubTaskInEpicTask);
        allSubTask.remove(idSubTask);
        updateEpicTask(allEpicTask.get(idEpicTask), idEpicTask);
    }

    // 3 метода по получению задач каждого из типов по ID
    Task getTaskById(int taskId) {
        return allTask.get(taskId);
    }

    EpicTask getEpicTaskById(int epicTaskId) {
        return allEpicTask.get(epicTaskId);
    }

    SubTask getSubTaskById(int subTaskId) {
        return allSubTask.get(subTaskId);
    }

    ArrayList<SubTask> getSubTaskOfACertainEpicTask(int epicTaskId) {
        ArrayList<Integer> allIdSubTask = allEpicTask.get(epicTaskId).getIdSubTask();
        ArrayList<SubTask> subTasksOfACertainEpicTask = new ArrayList<>();
        for (int idSubTask : allIdSubTask) {
            subTasksOfACertainEpicTask.add(allSubTask.get(idSubTask));
        }
        return subTasksOfACertainEpicTask;
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
