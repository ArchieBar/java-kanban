package managerTest;

import tasksTest.EpicTask;
import tasksTest.Status;
import tasksTest.SubTask;
import tasksTest.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {


    private int id = 0;
    protected final Map<Integer, Task> allTasks = new HashMap<>();
    protected final Map<Integer, EpicTask> allEpicTasks = new HashMap<>();
    protected final Map<Integer, SubTask> allSubTasks = new HashMap<>();
    protected final HistoryManager historyManager = Manager.getDefaultHistory();
    private final Map<LocalDateTime, Boolean> timeGrid = new TreeMap<>(fillInTimeGrid());
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.nullsLast(Comparator.comparing(Task::getStartTime)));

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Метод по созданию уникального идентификатора
     *
     * @return Возвращает уникальный id
     */
    private int createId() {
        return ++this.id;
    }

    private Map<LocalDateTime, Boolean> fillInTimeGrid() {
        Map<LocalDateTime, Boolean> newTimeGrid = new TreeMap<>();
        final LocalDateTime startTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        final LocalDateTime endTime = startTime.plusYears(5);
        LocalDateTime thisTime = startTime;
        while (!thisTime.isEqual(endTime)) {
            newTimeGrid.put(thisTime, true);
            thisTime = thisTime.plusMinutes(15);
        }
        return newTimeGrid;
    }

    private LocalDateTime findNearestDate(LocalDateTime dateTime) {
        final int minuteInterval = 15;
        int minutesForReturn;
        int remains = dateTime.getMinute() % minuteInterval;
        if (remains == 0) {
            return dateTime;
        } else if (remains <= (minuteInterval / 2)) {
            minutesForReturn = dateTime.getMinute() - remains;
        } else {
            minutesForReturn = dateTime.getMinute() + (minuteInterval - remains);
        }
        if (minutesForReturn == 60) {
            return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
                    dateTime.getHour() + 1, 0);
        } else return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
                dateTime.getHour(), minutesForReturn);

    }

    private void checkTimeIntersection(Task task) throws ManagerSaveException {
        boolean intermediaStatus = false;
        LocalDateTime time = findNearestDate(task.getStartTime());
        LocalDateTime endTime = findNearestDate(task.getEndTime());
        while (!time.isEqual(endTime)) {
            if (timeGrid.get(time)) {
                intermediaStatus = true;
                timeGrid.put(time, false);
            }
            time = time.plusMinutes(15);
        }
        if (intermediaStatus) {
            prioritizedTasks.add(task);
        } else throw new ManagerSaveException("Время задач не может пересекаться.");
    }

    private void removeBusyTime(Task oldTask) {
        LocalDateTime time = findNearestDate(oldTask.getStartTime());
        LocalDateTime endTime = findNearestDate(oldTask.getEndTime());
        while (!time.isEqual(endTime)) {
            if (!timeGrid.get(time)) {
                timeGrid.put(time, true);
            }
            time = time.plusMinutes(15);
        }
        prioritizedTasks.remove(oldTask);
    }

    /**
     * Метод по созданию Задачи и добавлению её в список всех задач
     */
    @Override
    public void createTask(Task task) throws ManagerSaveException {
        checkTimeIntersection(task);
        int thisId = createId();
        task.setId(thisId);
        allTasks.put(thisId, task);
    }

    /**
     * Метод по созданию Эпика и добавлению её в список всех задач
     */
    @Override
    public void createEpicTask(EpicTask epicTask) throws ManagerSaveException {
        int thisId = createId();
        epicTask.setId(thisId);
        allEpicTasks.put(thisId, epicTask);
    }

    /**
     * Создание подзадачи, в данном методе присваивается уникальный идентификатор для подзадачи, а так же
     * присваивается идентификатор эпика, которому принадлежит подзадача и добавление идентификатора в список
     * подзадач эпика
     */
    @Override
    public void createSubTask(SubTask subTask) throws ManagerSaveException {
        checkTimeIntersection(subTask);
        int epicId = subTask.getIdEpicTask();
        if (allEpicTasks.containsKey(epicId)) {
            int thisId = createId();
            subTask.setId(thisId);
            allSubTasks.put(thisId, subTask);

            EpicTask epicTask = allEpicTasks.get(epicId);
            epicTask.setSubTasksIds(thisId);
            updateStatusAndTimeOfEpicTask(epicTask);
        } else throw new ManagerSaveException("EpicTask with id: " + epicId + " not found.");
    }

    /**
     * Метод по обновлению Задачи
     */
    @Override
    public void updateTask(Task newTask, Task oldTask) throws ManagerSaveException {
        if (allTasks.get(oldTask.getId()) != null) {
            removeBusyTime(oldTask);
            checkTimeIntersection(newTask);
            newTask.setId(oldTask.getId());
            allTasks.put(newTask.getId(), newTask);
        }
    }

    /**
     * Метод по обновлению Эпика
     */
    @Override
    public void updateEpicTask(EpicTask newEpicTask, EpicTask oldEpicTask) throws ManagerSaveException {
        if (allEpicTasks.get(oldEpicTask.getId()) != null) {
            newEpicTask.setId(oldEpicTask.getId());
            newEpicTask.setSubTasksIds(oldEpicTask.getSubTasksIds());
            allEpicTasks.put(newEpicTask.getId(), newEpicTask);
        }

    }

    /**
     * Метод по обновлению Подзадачи, а так же обновление статуса Эпика
     */
    @Override
    public void updateSubTask(SubTask newSubTask, SubTask oldSubTask) throws ManagerSaveException {
        int idEpicTask = oldSubTask.getIdEpicTask();
        removeBusyTime(oldSubTask);
        if (allSubTasks.get(oldSubTask.getId()) != null && allEpicTasks.get(idEpicTask) != null) {
            checkTimeIntersection(newSubTask);
            newSubTask.setId(oldSubTask.getId());
            allSubTasks.put(oldSubTask.getId(), newSubTask);
            updateStatusAndTimeOfEpicTask(allEpicTasks.get(idEpicTask));
        }
    }

    /**
     * Обновление статуса Эпика построено на сравнении всех элементов массива с первым,
     * если все они равны первому, то присвоить Эпику статус первой подзадачи в массиве,
     * если нет, то присвоить эпику статут: "IN_PROGRESS".
     *
     * @param epicTask
     */
    private void updateStatusAndTimeOfEpicTask(EpicTask epicTask) {
        Status intermediateStatus;
        LocalDateTime intermediaStartTime;
        LocalDateTime intermediaEndTime;
        List<SubTask> subTasks = getSubTaskOfACertainEpicTask(epicTask.getId());

        if (subTasks != null && !subTasks.isEmpty()) {
            intermediateStatus = subTasks.get(0).getStatus();
            intermediaStartTime = subTasks.get(0).getStartTime();
            intermediaEndTime = subTasks.get(0).getEndTime();

            for (SubTask subTask : subTasks) {
                if (!(subTask.getStatus().equals(intermediateStatus))) {
                    intermediateStatus = Status.IN_PROGRESS;
                }

                if (intermediaEndTime.isBefore(subTask.getEndTime())) {
                    intermediaEndTime = subTask.getEndTime();
                }

                if (intermediaStartTime.isAfter(subTask.getStartTime())) {
                    intermediaStartTime = subTask.getStartTime();
                }
            }


        } else {
            intermediateStatus = Status.NEW;
            intermediaStartTime = null;
            intermediaEndTime = null;
        }

        epicTask.setStatus(intermediateStatus);
        epicTask.setStartTime(intermediaStartTime);
        epicTask.setEndTime(intermediaEndTime);

    }

    /**
     * Метод по удалению всех Задач
     */
    @Override
    public void removeAllTask() throws ManagerSaveException {
        for (Task task : allTasks.values()) {
            historyManager.remove(task.getId());
        }
        allTasks.clear();
    }

    /**
     * Удаление мапы Эпиков с Подзадачами, т.к. Подзадачи без Эпика существовать не могут.
     */
    @Override
    public void removeAllEpicTask() throws ManagerSaveException {
        for (EpicTask epicTask : allEpicTasks.values()) {
            historyManager.remove(epicTask.getId());
            for (int idSubTask : epicTask.getSubTasksIds()) {
                removeBusyTime(allSubTasks.get(idSubTask));
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
    public void removeAllSubTask() throws ManagerSaveException {
        for (EpicTask epicTask : allEpicTasks.values()) {
            epicTask.getSubTasksIds().clear();
            updateStatusAndTimeOfEpicTask(epicTask);
        }
        for (SubTask subTask : allSubTasks.values()) {
            removeBusyTime(subTask);
            historyManager.remove(subTask.getId());
        }
        allSubTasks.clear();
    }

    /**
     * Метод по удалению Задачи по id
     */
    @Override
    public void removeTaskById(Integer idTask) throws ManagerSaveException {
        historyManager.remove(idTask);
        removeBusyTime(getTaskById(idTask));
        allTasks.remove(idTask);
    }

    /**
     * Удаление отдельного Эпика, а также удаление связанных с ним Подзадач.
     */
    @Override
    public void removeEpicTaskById(Integer idEpicTask) throws ManagerSaveException {
        EpicTask epicTask = allEpicTasks.get(idEpicTask);
        for (int idSubTask : epicTask.getSubTasksIds()) {
            historyManager.remove(idSubTask);
            removeBusyTime(getSubTaskById(idSubTask));
            allSubTasks.remove(idSubTask);
        }
        historyManager.remove(idEpicTask);
        allEpicTasks.remove(idEpicTask);
    }

    /**
     * Удаление отдельной подзадачи, а так же редактирование списка 'idSubTask' и статуса у Эпика
     */
    @Override
    public void removeSubTaskById(Integer idSubTask) throws ManagerSaveException {
        int idEpicTask = allSubTasks.get(idSubTask).getIdEpicTask();
        EpicTask epicTask = allEpicTasks.get(idEpicTask);
        epicTask.getSubTasksIds().remove(idSubTask);
        historyManager.remove(idSubTask);
        removeBusyTime(getSubTaskById(idSubTask));
        allSubTasks.remove(idSubTask);
        updateStatusAndTimeOfEpicTask(allEpicTasks.get(idEpicTask));
    }

    /**
     * Метод по получению Задачи по Id, а так же добавление задачи в историю
     */
    @Override
    public Task getTaskById(Integer taskId) throws ManagerSaveException {
        historyManager.addHistory(allTasks.get(taskId));
        return allTasks.get(taskId);
    }

    /**
     * Метод по получению Эпика по Id, а так же добавление Эпика в историю
     */
    @Override
    public EpicTask getEpicTaskById(Integer epicTaskId) throws ManagerSaveException {
        historyManager.addHistory(allEpicTasks.get(epicTaskId));
        return allEpicTasks.get(epicTaskId);
    }

    /**
     * Метод по получению Подзадачи по Id, а так же добавление Подзадачи в историю
     */
    @Override
    public SubTask getSubTaskById(Integer subTaskId) throws ManagerSaveException {
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
    public List<SubTask> getSubTaskOfACertainEpicTask(Integer epicTaskId) {
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
     *
     * @return Task task
     * @return null при ошибке поиска
     */
    @Override
    public Task findTask(Integer idTask) {
        for (int id : allTasks.keySet()) {
            if (id == idTask) {
                return allTasks.get(idTask);
            }
        }
        for (int id : allSubTasks.keySet()) {
            if (id == idTask) {
                return allSubTasks.get(idTask);
            }
        }
        for (int id : allEpicTasks.keySet()) {
            if (id == idTask) {
                return allEpicTasks.get(idTask);
            }
        }
        return null;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return List.copyOf(prioritizedTasks);
    }
}
