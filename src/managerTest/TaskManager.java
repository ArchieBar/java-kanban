package managerTest;

import tasksTest.EpicTask;
import tasksTest.SubTask;
import tasksTest.Task;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public interface TaskManager {

    void createTask(Task task) throws ManagerSaveException;

    void createEpicTask(EpicTask epicTask) throws ManagerSaveException;

    void createSubTask(SubTask subTask) throws ManagerSaveException;

    void updateTask(Task newTask, Task oldTask) throws ManagerSaveException;

    void updateEpicTask(EpicTask newEpicTask, EpicTask oldEpicTask) throws ManagerSaveException;

    void updateSubTask(SubTask newSubTask, SubTask oldSubTask) throws ManagerSaveException;

    void removeAllTask() throws ManagerSaveException;

    void removeAllEpicTask() throws ManagerSaveException;

    void removeAllSubTask() throws ManagerSaveException;

    void removeTaskById(Integer idTask) throws ManagerSaveException;

    void removeEpicTaskById(Integer idEpicTask) throws ManagerSaveException;

    void removeSubTaskById(Integer idSubTask) throws ManagerSaveException;

    Task getTaskById(int taskId) throws ManagerSaveException;

    EpicTask getEpicTaskById(int epicTaskId) throws ManagerSaveException;

    SubTask getSubTaskById(int subTaskId) throws ManagerSaveException;

    List<Task> getHistory();

    List<SubTask> getSubTaskOfACertainEpicTask(int epicTaskId);

    List<Task> getAllTasks();

    List<EpicTask> getAllEpicTasks();

    List<SubTask> getAllSubTasks();

    Task findTask (int id);

    Set<Task> getPrioritizedTasks () throws ManagerSaveException;
}
