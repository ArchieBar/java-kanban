package manager;

import tasks.EpicTask;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {


    private int id = 0;
    protected final Map<Integer, Task> allTasks = new HashMap<>();
    protected final Map<Integer, EpicTask> allEpicTasks = new HashMap<>();
    protected final Map<Integer, SubTask> allSubTasks = new HashMap<>();
    protected final HistoryManager historyManager = Manager.getDefaultHistory();

    public void setId(int id) {
        this.id = id;
    }

    /** Метод по созданию уникального идентификатора
     * @return Возвращает уникальный id
     */
    private int createId() {
        return ++this.id;
    }

    /**
     * Метод по созданию Задачи и добавлению её в список всех задач
     */
    @Override
    public void createTask(Task task) {
        int thisId = createId();
        task.setId(thisId);
        task.setStatus(Status.NEW);
        allTasks.put(thisId, task);
    }

    /**
     * Метод по созданию Эпика и добавлению её в список всех задач
     */
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
            epicTask.setSubTasksIds(thisId);
            updateStatusEpicTask(epicTask);
        }
    }

    /**
     * Метод по обновлению Задачи
     */
    @Override
    public void updateTask(Task newTask) {
        if (allTasks.get(newTask.getId()) != null) {
            allTasks.put(newTask.getId(), newTask);
        }
    }

    /**
     * Метод по обновлению Эпика
     */
    @Override
    public void updateEpicTask(EpicTask newEpicTask) {
        if (allEpicTasks.get(newEpicTask.getId()) != null) {
            allEpicTasks.put(newEpicTask.getId(), newEpicTask);
        }

    }

    /**
     * Метод по обновлению Подзадачи, а так же обновление статуса Эпика
     */
    @Override
    public void updateSubTask(SubTask newSubTask) {
        int idEpicTask = newSubTask.getIdEpicTask();
        if (allSubTasks.get(newSubTask.getId()) != null && allEpicTasks.get(idEpicTask) != null) {
            allSubTasks.put(newSubTask.getId(), newSubTask);
            updateStatusEpicTask(allEpicTasks.get(idEpicTask));
        }
    }

    /**
     * Обновление статуса Эпика построено на сравнении всех элементов массива с первым,
     * если все они равны первому, то присвоить Эпику статус первой подзадачи в массиве,
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
     * Метод по удалению всех Задач
     */
    @Override
    public void removeAllTask() {
        for (Task task : allTasks.values()) {
            historyManager.remove(task.getId());
        }
        allTasks.clear();
    }

    /**
     * Удаление мапы Эпиков с Подзадачами, т.к. Подзадачи без Эпика существовать не могут.
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

    /**
     * Удаление всех Подзадач с установкой статуса "NEW" для Эпика
     */
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
     * Метод по удалению Задачи по id
     */
    @Override
    public void removeTaskById(Integer idTask) {
        historyManager.remove(idTask);
        allTasks.remove(idTask);
    }

    /**
     * Удаление отдельного Эпика, а также удаление связанных с ним Подзадач.
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
     * Удаление отдельной подзадачи, а так же редактирование списка 'idSubTask' и статуса у Эпика
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
     * Метод по получению Задачи по Id, а так же добавление задачи в историю
     */
    @Override
    public Task getTaskById(int taskId) {
        historyManager.addHistory(allTasks.get(taskId));
        return allTasks.get(taskId);
    }

    /**
     * Метод по получению Эпика по Id, а так же добавление Эпика в историю
     */
    @Override
    public EpicTask getEpicTaskById(int epicTaskId) {
        historyManager.addHistory(allEpicTasks.get(epicTaskId));
        return allEpicTasks.get(epicTaskId);
    }

    /**
     * Метод по получению Подзадачи по Id, а так же добавление Подзадачи в историю
     */
    @Override
    public SubTask getSubTaskById(int subTaskId) {
        historyManager.addHistory(allSubTasks.get(subTaskId));
        return allSubTasks.get(subTaskId);
    }

    /**
     * Метод для получения истории просмотров задач по ID.
     * Логика метода представлена в классе: manager.InMemoryHistoryManager.
     */
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /**
     * Метод по возвращению всех подзадач определённого эпика.
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
     * Метод по возвращению списка Задач
     */
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    /**
     * Метод по возвращению списка Эпиков
     */
    @Override
    public List<EpicTask> getAllEpicTasks() {
        return new ArrayList<>(allEpicTasks.values());
    }

    /**
     * Метод по возвращению списка Подзадач
     */
    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(allSubTasks.values());
    }

    /**
     * Метод по нахождению задачи по id из всех списков Задач, Эпиков и Подзадач
     * @return Task task
     * @return null при ошибке поиска
     */
    @Override
    public Task findTask (int idTask) {
        for (int id : allTasks.keySet()) {
            if (id == idTask) return getTaskById(idTask);
        }
        for (int id : allSubTasks.keySet()) {
            if (id == idTask) return getSubTaskById(idTask);
        }
        for (int id : allEpicTasks.keySet()) {
            if (id == idTask) return getEpicTaskById(idTask);
        }
        return null;
    }
}
