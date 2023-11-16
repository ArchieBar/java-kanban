package manager;

import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    void createTask(Task task) throws ManagerSaveException;

    void createEpicTask(EpicTask epicTask) throws ManagerSaveException;

    void createSubTask(SubTask subTask) throws ManagerSaveException;

    void updateTask(Task newTask) throws ManagerSaveException;

    void updateEpicTask(EpicTask newEpicTask) throws ManagerSaveException;

    void updateSubTask(SubTask newSubTask) throws ManagerSaveException;

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
}
