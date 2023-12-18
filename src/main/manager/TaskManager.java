package main.manager;

import main.http.HttpTaskManager;
import main.tasks.EpicTask;
import main.tasks.SubTask;
import main.tasks.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {

    void createTask(Task task) throws IOException;

    void createEpicTask(EpicTask epicTask) throws IOException;

    void createSubTask(SubTask subTask) throws IOException;

    void updateTask(Task newTask, Task oldTask) throws IOException;

    void updateEpicTask(EpicTask newEpicTask, EpicTask oldEpicTask) throws IOException;

    void updateSubTask(SubTask newSubTask, SubTask oldSubTask) throws IOException;

    void removeAllTask() throws IOException;

    void removeAllEpicTask() throws IOException;

    void removeAllSubTask() throws IOException;

    void removeTaskById(Integer idTask) throws IOException;

    void removeEpicTaskById(Integer idEpicTask) throws IOException;

    void removeSubTaskById(Integer idSubTask) throws IOException;

    Task getTaskById(Integer taskId) throws IOException;

    EpicTask getEpicTaskById(Integer epicTaskId) throws IOException;

    SubTask getSubTaskById(Integer subTaskId) throws IOException;

    List<Task> getHistory();

    List<SubTask> getSubTaskOfACertainEpicTask(Integer epicTaskId);

    List<Task> getAllTasks();

    List<EpicTask> getAllEpicTasks();

    List<SubTask> getAllSubTasks();

    Task findTask(Integer id);

    void deleteById(Integer id) throws IOException;

    List<Task> getPrioritizedTasks() throws ManagerSaveException;
}
