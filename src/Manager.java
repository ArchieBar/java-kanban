import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id = 0;
    private HashMap<Integer, Task> allTask = new HashMap<>();
    private HashMap<Integer, EpicTask> allEpicTask = new HashMap<>();
    private HashMap<Integer, SubTask> allSubTask = new HashMap<>();

    // Метод по созданию уникального идентификатора.
    private int createId() {
        this.id++;
        return id;
    }

    // 3 метода с разной сигнатурой по созданию новой задачи / эпика / подзадачи.
    void createTask(Task task) {
        int thisId = createId();
        task.setId(thisId);
        task.setStatus("NEW");
        allTask.put(thisId, task);
    }

    void createEpicTask(EpicTask epicTask) {
        int thisId = createId();
        epicTask.setId(thisId);
        epicTask.setStatus("NEW");
        allEpicTask.put(thisId, epicTask);
    }

    // Создание подзадачи, в данном методе присваивается уникальный идентификатор для подзадачи, а так же
    // присваивается идентификатор эпика, которому принадлежит подзадача и добавление идентификатора в список
    // подзадач эпика
    void createSubTask(SubTask subTask) {
        int thisId = createId();
        int epicId = subTask.getIdEpicTask();
        if (allEpicTask.containsKey(epicId)) {
            subTask.setId(thisId);
            subTask.setStatus("NEW");
            allSubTask.put(thisId, subTask);

            EpicTask epicTask = allEpicTask.get(epicId);
            epicTask.getIdSubTask().add(thisId);
            updateStatusEpicTask(epicTask);
        }
    }

    // 3 метода по обновлению задач для каждого из типов.
    void updateTask(Task newTask) {
        allTask.put(newTask.getId(), newTask);
    }

    void updateEpicTask(EpicTask newEpicTask) {
        allEpicTask.put(newEpicTask.getId(), newEpicTask);
    }

    // Обновление статуса эпика построено на сравнении всех элементов массива с первым
    // Если все они равны первому, то присвоить эпику статус первой подзадачи в массиве
    // Если нет, то присвоить эпику статут: "IN_PROGRESS".
    private void updateStatusEpicTask(EpicTask epicTask) {
        String intermediateStatus;

        if (epicTask.getIdSubTask() != null) {
            intermediateStatus = allSubTask.get(epicTask.getIdSubTask().get(0)).getStatus();
            for (Integer idSubTask : epicTask.getIdSubTask()) {
                if (!(allSubTask.get(idSubTask).getStatus().equalsIgnoreCase(intermediateStatus))) {
                    intermediateStatus = "IN_PROGRESS";
                }
            }
        } else {
            intermediateStatus = "NEW";
        }

        epicTask.setStatus(intermediateStatus);
    }

    void updateSubTask(SubTask newSubTask) {
        int idEpicTask = newSubTask.getIdEpicTask();
        allSubTask.put(newSubTask.getId(), newSubTask);
        updateStatusEpicTask(allEpicTask.get(idEpicTask));
    }

    // 3 метода по удалению каждой отдельной мапы по типу задачи.
    void removeAllTask() {
        allTask.clear();
    }

    // Удаление мапы эпиков с подзадачами, т.к. подзадачи без эпика существовать не могут.
    void removeAllEpicTask() {
        allEpicTask.clear();
        allSubTask.clear();
    }

    void removeAllSubTask() {
        for (EpicTask epicTask : allEpicTask.values()) {
            epicTask.getIdSubTask().clear();
            epicTask.setStatus("NEW");
        }
        allSubTask.clear();
    }

    // 3 метода по удалению задач по их идентификатору.
    void removeTaskById(Integer idTask) {
        allTask.remove(idTask);
    }

    // Удаление отдельного эпика, а также удаление связанных с ним подзадач.
    void removeEpicTaskById(Integer idEpicTask) {
        EpicTask epicTask = allEpicTask.get(idEpicTask);
        for (Integer id : epicTask.getIdSubTask()) {
            allSubTask.remove(id);
        }
        allEpicTask.remove(idEpicTask);
    }

    // Удаление отдельной подзадачи, а так же редактирование списка 'idSubTask' у эпика
    void removeSubTaskById(Integer idSubTask) {
        int idEpicTask = allSubTask.get(idSubTask).getIdEpicTask();
        EpicTask epicTask = allEpicTask.get(idEpicTask);
        epicTask.getIdSubTask().remove(idSubTask);
        allSubTask.remove(idSubTask);
        updateStatusEpicTask(allEpicTask.get(idEpicTask));
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

    public ArrayList<Task> getAllTask() {
        ArrayList<Task> allTaskList = new ArrayList<>();
        for (Task task : allTask.values()) {
            allTaskList.add(task);
        }
        return allTaskList;
    }

    public ArrayList<Task> getAllEpicTask() {
        ArrayList<Task> allEpicTaskList = new ArrayList<>();
        for (Task task : allEpicTask.values()) {
            allEpicTaskList.add(task);
        }
        return allEpicTaskList;
    }

    public ArrayList<Task> getAllSubTask() {
        ArrayList<Task> allSubTaskList = new ArrayList<>();
        for (Task task : allSubTask.values()) {
            allSubTaskList.add(task);
        }
        return allSubTaskList;
    }
}
