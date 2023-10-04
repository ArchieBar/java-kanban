import java.util.ArrayList;

public interface TaskManager {

    // 3 метода с разной сигнатурой по созданию новой задачи / эпика / подзадачи.
    void createTask(Task task);

    void createEpicTask(EpicTask epicTask);

    // Создание подзадачи, в данном методе присваивается уникальный идентификатор для подзадачи, а так же
    // присваивается идентификатор эпика, которому принадлежит подзадача и добавление идентификатора в список
    // подзадач эпика
    void createSubTask(SubTask subTask);

    // 3 метода по обновлению задач для каждого из типов.
    void updateTask(Task newTask);

    void updateEpicTask(EpicTask newEpicTask);

    // Обновление статуса эпика построено на сравнении всех элементов массива с первым
    // Если все они равны первому, то присвоить эпику статус первой подзадачи в массиве
    // Если нет, то присвоить эпику статут: "IN_PROGRESS".

    void updateSubTask(SubTask newSubTask);

    // 3 метода по удалению каждой отдельной мапы по типу задачи.
    void removeAllTask();

    // Удаление мапы эпиков с подзадачами, т.к. подзадачи без эпика существовать не могут.
    void removeAllEpicTask();

    void removeAllSubTask();

    // 3 метода по удалению задач по их идентификатору.
    void removeTaskById(Integer idTask);

    // Удаление отдельного эпика, а также удаление связанных с ним подзадач.
    void removeEpicTaskById(Integer idEpicTask);

    // Удаление отдельной подзадачи, а так же редактирование списка 'idSubTask' у эпика
    void removeSubTaskById(Integer idSubTask);

    // 3 метода по получению задач каждого из типов по ID
    Task getTaskById(int taskId);

    EpicTask getEpicTaskById(int epicTaskId);

    SubTask getSubTaskById(int subTaskId);

    public ArrayList<Task> getHistory();

    ArrayList<SubTask> getSubTaskOfACertainEpicTask(int epicTaskId);

    ArrayList<Task> getAllTask();

    ArrayList<EpicTask> getAllEpicTask();

    ArrayList<SubTask> getAllSubTask();
}
