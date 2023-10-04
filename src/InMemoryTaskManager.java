import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final HashMap<Integer, Task> allTask = new HashMap<>();
    private final HashMap<Integer, EpicTask> allEpicTask = new HashMap<>();
    private final HashMap<Integer, SubTask> allSubTask = new HashMap<>();
    private final HistoryManager historyManager = Manager.getDefaultHistory();

    /**
     * <p>Метод по созданию уникального идентификатора</p>
     * @return Возвращает уникальный id
     */
    private int createId() {
        return ++this.id;
    }

    // 3 метода с разной сигнатурой по созданию новой задачи / эпика / подзадачи.
    @Override
    public void createTask(Task task) {
        int thisId = createId();
        task.setId(thisId);
        task.setStatus(Status.NEW);
        allTask.put(thisId, task);
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        int thisId = createId();
        epicTask.setId(thisId);
        epicTask.setStatus(Status.NEW);
        allEpicTask.put(thisId, epicTask);
    }

    // Создание подзадачи, в данном методе присваивается уникальный идентификатор для подзадачи, а так же
    // присваивается идентификатор эпика, которому принадлежит подзадача и добавление идентификатора в список
    // подзадач эпика
    @Override
    public void createSubTask(SubTask subTask) {
        int thisId = createId();
        int epicId = subTask.getIdEpicTask();
        if (allEpicTask.containsKey(epicId)) {
            subTask.setId(thisId);
            subTask.setStatus(Status.NEW);
            allSubTask.put(thisId, subTask);

            EpicTask epicTask = allEpicTask.get(epicId);
            epicTask.getSubTasksIds().add(thisId);
            updateStatusEpicTask(epicTask);
        }
    }

    // 3 метода по обновлению задач для каждого из типов.
    @Override
    public void updateTask(Task newTask) {
        allTask.put(newTask.getId(), newTask);
    }

    @Override
    public void updateEpicTask(EpicTask newEpicTask) {
        allEpicTask.put(newEpicTask.getId(), newEpicTask);
    }

    // Обновление статуса эпика построено на сравнении всех элементов массива с первым
    // Если все они равны первому, то присвоить эпику статус первой подзадачи в массиве
    // Если нет, то присвоить эпику статут: "IN_PROGRESS".
    private void updateStatusEpicTask(EpicTask epicTask) {
        Status intermediateStatus;
        ArrayList<Integer> subTasksIds = epicTask.getSubTasksIds();

        if (subTasksIds != null && !subTasksIds.isEmpty()) {
            intermediateStatus = allSubTask.get(epicTask.getSubTasksIds().get(0)).getStatus();
            for (Integer idSubTask : epicTask.getSubTasksIds()) {
                if (!(allSubTask.get(idSubTask).getStatus().equals(intermediateStatus))) {
                    intermediateStatus = Status.IN_PROGRESS;
                }
            }
        } else {
            intermediateStatus = Status.NEW;
        }

        epicTask.setStatus(intermediateStatus);
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        int idEpicTask = newSubTask.getIdEpicTask();
        allSubTask.put(newSubTask.getId(), newSubTask);
        updateStatusEpicTask(allEpicTask.get(idEpicTask));
    }

    // 3 метода по удалению каждой отдельной мапы по типу задачи.
    @Override
    public void removeAllTask() {
        allTask.clear();
    }

    // Удаление мапы эпиков с подзадачами, т.к. подзадачи без эпика существовать не могут.
    @Override
    public void removeAllEpicTask() {
        allEpicTask.clear();
        allSubTask.clear();
    }

    @Override
    public void removeAllSubTask() {
        for (EpicTask epicTask : allEpicTask.values()) {
            epicTask.getSubTasksIds().clear();
            epicTask.setStatus(Status.NEW);
        }
        allSubTask.clear();
    }

    // 3 метода по удалению задач по их идентификатору.
    @Override
    public void removeTaskById(Integer idTask) {
        allTask.remove(idTask);
    }

    // Удаление отдельного эпика, а также удаление связанных с ним подзадач.
    @Override
    public void removeEpicTaskById(Integer idEpicTask) {
        EpicTask epicTask = allEpicTask.get(idEpicTask);
        for (Integer id : epicTask.getSubTasksIds()) {
            allSubTask.remove(id);
        }
        allEpicTask.remove(idEpicTask);
    }

    // Удаление отдельной подзадачи, а так же редактирование списка 'idSubTask' у эпика
    @Override
    public void removeSubTaskById(Integer idSubTask) {
        int idEpicTask = allSubTask.get(idSubTask).getIdEpicTask();
        EpicTask epicTask = allEpicTask.get(idEpicTask);
        epicTask.getSubTasksIds().remove(idSubTask);
        allSubTask.remove(idSubTask);
        updateStatusEpicTask(allEpicTask.get(idEpicTask));
    }

    // 3 метода по получению задач каждого из типов по ID
    @Override
    public Task getTaskById(int taskId) {
        historyManager.addHistory(allTask.get(taskId));
        return allTask.get(taskId);
    }

    @Override
    public EpicTask getEpicTaskById(int epicTaskId) {
        historyManager.addHistory(allEpicTask.get(epicTaskId));
        return allEpicTask.get(epicTaskId);
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) {
        historyManager.addHistory(allSubTask.get(subTaskId));
        return allSubTask.get(subTaskId);
    }

    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<SubTask> getSubTaskOfACertainEpicTask(int epicTaskId) {
        ArrayList<Integer> allIdSubTask = allEpicTask.get(epicTaskId).getSubTasksIds();
        ArrayList<SubTask> subTasksOfACertainEpicTask = new ArrayList<>();
        for (int idSubTask : allIdSubTask) {
            subTasksOfACertainEpicTask.add(allSubTask.get(idSubTask));
        }
        return subTasksOfACertainEpicTask;
    }

    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<Task>(allTask.values());
    }

    @Override
    public ArrayList<EpicTask> getAllEpicTask() {
        return new ArrayList<EpicTask>(allEpicTask.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        return new ArrayList<SubTask>(allSubTask.values());
    }
}
