package Manager;

import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final HashMap<Integer, Task> allTasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> allEpicTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> allSubTasks = new HashMap<>();
    private final HistoryManager historyManager = Manager.getDefaultHistory();

    /**<p>Метод по созданию уникального идентификатора</p>
     * @return Возвращает уникальный id
     */
    private int createId() {
        return ++this.id;
    }

    /**
     * Методы:
     * createTask(Tasks.Task task),
     * createEpicTask(Tasks.EpicTask epicTask),
     * createSubTask(Tasks.SubTask subTask),
     * по созданию новой задачи / эпика / подзадачи.
     */
    @Override
    public void createTask(Task task) {
        int thisId = createId();
        task.setId(thisId);
        task.setStatus(Status.NEW);
        allTasks.put(thisId, task);
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        int thisId = createId();
        epicTask.setId(thisId);
        epicTask.setStatus(Status.NEW);
        allEpicTasks.put(thisId, epicTask);
    }

    /**
     * Создание подзадачи, в данном методе присваивается уникальный идентификатор для подзадачи, а так же
     * присваивается идентификатор эпика, которому принадлежит подзадача и добавление идентификатора в список
     * подзадач эпика
     * @param subTask
     */
    @Override
    public void createSubTask(SubTask subTask) {
        int thisId = createId();
        int epicId = subTask.getIdEpicTask();
        if (allEpicTasks.containsKey(epicId)) {
            subTask.setId(thisId);
            subTask.setStatus(Status.NEW);
            allSubTasks.put(thisId, subTask);

            EpicTask epicTask = allEpicTasks.get(epicId);
            epicTask.getSubTasksIds().add(thisId);
            updateStatusEpicTask(epicTask);
        }
    }

    /**
     * Методы:
     * updateTask(Tasks.Task newTask),
     * updateEpicTask(Tasks.EpicTask newEpicTask),
     * updateSubTask(Tasks.SubTask newSubTask),
     * по обновлению задач для каждого из типов.
     */
    @Override
    public void updateTask(Task newTask) {
        if (allTasks.get(newTask.getId()) != null) {
            allTasks.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void updateEpicTask(EpicTask newEpicTask) {
        if (allEpicTasks.get(newEpicTask.getId()) != null) {
            allEpicTasks.put(newEpicTask.getId(), newEpicTask);
        }

    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        int idEpicTask = newSubTask.getIdEpicTask();
        if (allSubTasks.get(newSubTask.getId()) != null && allEpicTasks.get(idEpicTask) != null) {
            allSubTasks.put(newSubTask.getId(), newSubTask);
            updateStatusEpicTask(allEpicTasks.get(idEpicTask));
        }
    }

    /**
     * Обновление статуса эпика построено на сравнении всех элементов массива с первым,
     * если все они равны первому, то присвоить эпику статус первой подзадачи в массиве,
     * если нет, то присвоить эпику статут: "IN_PROGRESS".
     * @param epicTask
     */
    private void updateStatusEpicTask(EpicTask epicTask) {
        Status intermediateStatus;
        ArrayList<Integer> subTasksIds = epicTask.getSubTasksIds();

        if (subTasksIds != null && !subTasksIds.isEmpty()) {
            intermediateStatus = allSubTasks.get(epicTask.getSubTasksIds().get(0)).getStatus();
            for (Integer idSubTask : epicTask.getSubTasksIds()) {
                if (!(allSubTasks.get(idSubTask).getStatus().equals(intermediateStatus))) {
                    intermediateStatus = Status.IN_PROGRESS;
                }
            }
        } else {
            intermediateStatus = Status.NEW;
        }

        epicTask.setStatus(intermediateStatus);
    }

    /**
     * Методы:
     * removeAllTask(),
     * removeAllEpicTask(),
     * removeAllSubTask(),
     * по удалению каждой отдельной мапы по типу задачи
     */
    @Override
    public void removeAllTask() {
        for (Task task : allTasks.values()) {
            historyManager.remove(task.getId());
        }
        allTasks.clear();
    }

    /**
     * Удаление мапы эпиков с подзадачами, т.к. подзадачи без эпика существовать не могут.
     */
    @Override
    public void removeAllEpicTask() {
        for (EpicTask epicTask : allEpicTasks.values()) {
            historyManager.remove(epicTask.getId());
            for (int idSubTask : epicTask.getSubTasksIds()) {
                historyManager.remove(idSubTask);
            }
        }
        allEpicTasks.clear();
        allSubTasks.clear();
    }

    @Override
    public void removeAllSubTask() {
        for (EpicTask epicTask : allEpicTasks.values()) {
            epicTask.getSubTasksIds().clear();
            epicTask.setStatus(Status.NEW);
        }
        for (SubTask subTask : allSubTasks.values()) {
            historyManager.remove(subTask.getId());
        }
        allSubTasks.clear();
    }

    /**
     * Методы:
     * .removeTaskById(Integer idTask),
     * .removeEpicTaskById(Integer idEpicTask),
     * .removeSubTaskById(Integer idSubTask),
     * по удалению задач по их идентификатору.
     */
    @Override
    public void removeTaskById(Integer idTask) {
        historyManager.remove(idTask);
        allTasks.remove(idTask);
    }

    /**
     * Удаление отдельного эпика, а также удаление связанных с ним подзадач.
     * @param idEpicTask
     */
    @Override
    public void removeEpicTaskById(Integer idEpicTask) {
        EpicTask epicTask = allEpicTasks.get(idEpicTask);
        for (Integer id : epicTask.getSubTasksIds()) {
            historyManager.remove(id);
            allSubTasks.remove(id);
        }
        historyManager.remove(idEpicTask);
        allEpicTasks.remove(idEpicTask);
    }

    /**
     * Удаление отдельной подзадачи, а так же редактирование списка 'idSubTask' у эпика
     * @param idSubTask
     */
    @Override
    public void removeSubTaskById(Integer idSubTask) {
        int idEpicTask = allSubTasks.get(idSubTask).getIdEpicTask();
        EpicTask epicTask = allEpicTasks.get(idEpicTask);
        epicTask.getSubTasksIds().remove(idSubTask);
        historyManager.remove(idSubTask);
        allSubTasks.remove(idSubTask);
        updateStatusEpicTask(allEpicTasks.get(idEpicTask));
    }

    /**
     * Методы:
     * getTaskById(int taskId),
     * getEpicTaskById(int epicTaskId),
     * getSubTaskById(int subTaskId),
     * по получению задач каждого из типов по ID.
     */
    @Override
    public Task getTaskById(int taskId) {
        historyManager.addHistory(allTasks.get(taskId));
        return allTasks.get(taskId);
    }

    @Override
    public EpicTask getEpicTaskById(int epicTaskId) {
        historyManager.addHistory(allEpicTasks.get(epicTaskId));
        return allEpicTasks.get(epicTaskId);
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) {
        historyManager.addHistory(allSubTasks.get(subTaskId));
        return allSubTasks.get(subTaskId);
    }

    /**
     * Метод для получения истории просмотров задач по ID.
     * Логика метода представлена в классе: Manager.InMemoryHistoryManager.
     * @return Копия листа истории
     */
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /**
     * Метод по возвращению всех подзадач определённого эпика.
     * @param epicTaskId
     * @return Лист подзадач эпика
     */
    @Override
    public List<SubTask> getSubTaskOfACertainEpicTask(int epicTaskId) {
        ArrayList<Integer> allIdSubTask = allEpicTasks.get(epicTaskId).getSubTasksIds();
        ArrayList<SubTask> subTasksOfACertainEpicTask = new ArrayList<>();
        for (int idSubTask : allIdSubTask) {
            subTasksOfACertainEpicTask.add(allSubTasks.get(idSubTask));
        }
        return subTasksOfACertainEpicTask;
    }

    /**
     * Методы:
     * getAllTasks(),
     * getAllEpicTasks(),
     * getAllSubTasks(),
     * по возвращению листов задач определённого типа.
     */
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    @Override
    public List<EpicTask> getAllEpicTasks() {
        return new ArrayList<>(allEpicTasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(allSubTasks.values());
    }
}
