package Manager;

import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

import java.util.List;

public interface TaskManager {

    void createTask(Task task);

    void createEpicTask(EpicTask epicTask);

    void createSubTask(SubTask subTask);

    void updateTask(Task newTask);

    void updateEpicTask(EpicTask newEpicTask);

    void updateSubTask(SubTask newSubTask);

    void removeAllTask();

    void removeAllEpicTask();

    void removeAllSubTask();

    void removeTaskById(Integer idTask);

    void removeEpicTaskById(Integer idEpicTask);

    void removeSubTaskById(Integer idSubTask);

    Task getTaskById(int taskId);

    EpicTask getEpicTaskById(int epicTaskId);

    SubTask getSubTaskById(int subTaskId);

    List<Task> getHistory();

    List<SubTask> getSubTaskOfACertainEpicTask(int epicTaskId);

    List<Task> getAllTasks();

    List<EpicTask> getAllEpicTasks();

    List<SubTask> getAllSubTasks();
}
