package managerTest;

import org.junit.jupiter.api.Test;
import tasksTest.EpicTask;
import tasksTest.Status;
import tasksTest.SubTask;
import tasksTest.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractTaskManagerTest<T extends TaskManager> {
    T taskManager;

    @Test
    void checkingCorrectnessOfTaskCreation() throws ManagerSaveException {
        Task task = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task);
        assertEquals(task.getId(), 1);
        assertEquals(task, taskManager.getAllTasks().get(0));
    }

    @Test
    void checkingCorrectnessOfEpicTaskCreation() throws ManagerSaveException {
        EpicTask epicTask = new EpicTask("Task", "Description");
        taskManager.createEpicTask(epicTask);
        assertEquals(epicTask.getId(), 1);
        assertEquals(epicTask, taskManager.getAllEpicTasks().get(0));
    }

    @Test
    void checkingCorrectnessOfSubTaskCreation() throws ManagerSaveException {
        final IOException exception_1 = assertThrows(IOException.class,
                () -> taskManager.createSubTask(
                        new SubTask("SubTask", "Description", -1,
                                LocalDateTime.of(2022, 10, 10, 10, 0),
                                Duration.ofMinutes(15))));
        assertEquals(exception_1.getMessage(), "EpicTask with id: -1 not found.");
        assertEquals(taskManager.getAllSubTasks().size(), 0);

        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        taskManager.createEpicTask(epicTask);
        SubTask subTask = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2023, 10, 10, 10, 0),Duration.ofMinutes(15));
        taskManager.createSubTask(subTask);
        assertEquals(subTask.getId(), 2);
        assertEquals(taskManager.getAllSubTasks().get(0), subTask);
    }

    @Test
    void checkingForCorrectnessOfTaskUpdate() throws ManagerSaveException{
        Task task = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task);
        assertEquals(taskManager.getAllTasks().get(0), task);

        Task newTask = new Task(task.getName(), task.getDescription(), Status.IN_PROGRESS,
                task.getStartTime(), task.getDuration());
        taskManager.updateTask(newTask, task);
        assertEquals(taskManager.getAllTasks().get(0), newTask);
    }

    @Test
    void checkingForCorrectnessOfEpicTaskUpdate() throws ManagerSaveException {
        EpicTask epicTask = new EpicTask("Task", "Description");
        taskManager.createEpicTask(epicTask);
        assertEquals(taskManager.getAllEpicTasks().get(0), epicTask);

        EpicTask newEpicTask = new EpicTask("EpicTask", epicTask.getDescription());
        taskManager.updateEpicTask(newEpicTask, epicTask);
        assertEquals(taskManager.getAllEpicTasks().get(0), newEpicTask);
    }

    @Test
    void checkingForCorrectnessOfSubTaskUpdate() throws ManagerSaveException{
        EpicTask epicTask = new EpicTask("Task", "Description");
        taskManager.createEpicTask(epicTask);
        SubTask subTask = new SubTask("SubTask", "Description", 1,
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask);
        assertEquals(taskManager.getAllSubTasks().get(0), subTask);
        assertEquals(epicTask.getStatus(), Status.NEW);

        SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(),
                subTask.getIdEpicTask(), Status.DONE, subTask.getStartTime(), Duration.ofMinutes(15));
        taskManager.updateSubTask(newSubTask, subTask);
        assertEquals(taskManager.getAllSubTasks().get(0), newSubTask);
        assertEquals(epicTask.getStatus(), Status.DONE);
    }

    @Test
    void checkingForCorrectnessOfDeletingAllTasks() throws ManagerSaveException{
        Task task1 = new Task("Task", "Description",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        Task task2 = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        Task task3 = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        assertEquals(taskManager.getAllTasks().size(), 3);

        taskManager.removeAllTask();
        assertEquals(taskManager.getAllTasks().size(), 0);
    }

    @Test
    void checkingForCorrectnessOfDeletingAllEpicTasks() throws ManagerSaveException{
        EpicTask epicTask1 = new EpicTask("EpicTask", "Description");
        EpicTask epicTask2 = new EpicTask("EpicTask", "Description");
        EpicTask epicTask3 = new EpicTask("EpicTask", "Description");
        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);
        taskManager.createEpicTask(epicTask3);
        SubTask subTask1 = new SubTask("SubTask", "Description", 1,
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("SubTask", "Description", 1,
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        assertEquals(taskManager.getAllEpicTasks().size(), 3);
        assertEquals(taskManager.getAllSubTasks().size(), 2);

        taskManager.removeAllEpicTask();
        assertEquals(taskManager.getAllEpicTasks().size(), 0);
        assertEquals(taskManager.getAllSubTasks().size(), 0);
    }

    @Test
    void checkingForCorrectnessOfDeletingAllSubTasks() throws ManagerSaveException{
        EpicTask epicTask1 = new EpicTask("EpicTask", "Description");
        EpicTask epicTask2 = new EpicTask("EpicTask", "Description");
        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);
        SubTask subTask1 = new SubTask("SubTask", "Description", 1,
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("SubTask", "Description", 1,
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        SubTask subTask3 = new SubTask("SubTask", "Description", 2,
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        assertEquals(taskManager.getAllSubTasks().size(), 3);
        assertEquals(taskManager.getEpicTaskById(1).getSubTasksIds().size(), 2);

        taskManager.removeAllSubTask();
        assertEquals(taskManager.getAllSubTasks().size(), 0);
        assertEquals(taskManager.getEpicTaskById(1).getSubTasksIds().size(), 0);
    }

    @Test
    void checkingForCorrectnessOfDeletingATaskById() throws ManagerSaveException{
        Task task1 = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        Task task2 = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(taskManager.getAllTasks().size(), 2);

        taskManager.removeTaskById(task1.getId());
        assertFalse(taskManager.getAllTasks().contains(task1));
    }

    @Test
    void CheckingForCorrectnessOfDeletingAnEpicByIdAndDeletingAllItsSubTasks() throws ManagerSaveException {
        EpicTask epicTask1 = new EpicTask("Task", "Description");
        EpicTask epicTask2 = new EpicTask("Task", "Description");
        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);
        SubTask subTask = new SubTask("SubTask", "Description", 1,
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask);
        assertEquals(taskManager.getAllEpicTasks().size(), 2);
        assertEquals(taskManager.getAllSubTasks().size(), 1);

        taskManager.removeEpicTaskById(epicTask1.getId());
        assertFalse(taskManager.getAllEpicTasks().contains(epicTask1));
        assertEquals(taskManager.getAllSubTasks().size(), 0);
    }

    @Test
    void checkingForCorrectnessOfDeletingSubTasksByIdAndEditingListOfIdsOfAssociatedEpic() throws ManagerSaveException {
        EpicTask epicTask = new EpicTask("Task", "Description");
        taskManager.createEpicTask(epicTask);
        SubTask subTask1 = new SubTask("SubTask", "Description", 1,
                LocalDateTime.of(2020, 10, 10, 10, 10), Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("SubTask", "Description", 1,
                LocalDateTime.of(2021, 10, 10, 10, 5), Duration.ofMinutes(15));
        SubTask subTask3 = new SubTask("SubTask", "Description", 1,
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        assertEquals(taskManager.getAllEpicTasks().size(), 1);
        assertEquals(epicTask.getSubTasksIds().size(), 3);
        assertEquals(taskManager.getAllSubTasks().size(), 3);

        taskManager.removeSubTaskById(subTask1.getId());
        assertEquals(taskManager.getAllSubTasks().size(), 2);
        assertEquals(epicTask.getSubTasksIds().size(), 2);
        assertFalse(epicTask.getSubTasksIds().contains(subTask1.getId()));
    }

    @Test
    void checkingForCorrectnessOfReturnedTask() throws ManagerSaveException {
        Task task1 = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 40), Duration.ofMinutes(15));
        Task task2 = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 39), Duration.ofMinutes(15));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(taskManager.getAllTasks().size(), 2);

        assertEquals(taskManager.getTaskById(task1.getId()), task1);
    }

    @Test
    void checkingForCorrectnessOfReturnedEpicTask() throws ManagerSaveException{
        EpicTask epicTask1 = new EpicTask("EpicTask", "Description");
        EpicTask epicTask2 = new EpicTask("EpicTask", "Description");
        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);
        assertEquals(taskManager.getAllEpicTasks().size(), 2);

        assertEquals(taskManager.getEpicTaskById(epicTask1.getId()), epicTask1);
    }

    @Test
    void checkingForCorrectnessOfReturnedSubTask() throws ManagerSaveException{
        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        taskManager.createEpicTask(epicTask);
        SubTask subTask1 = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        assertEquals(taskManager.getAllEpicTasks().size(), 1);
        assertEquals(taskManager.getAllSubTasks().size(), 2);

        assertEquals(taskManager.getSubTaskById(subTask1.getId()), subTask1);


    }

    @Test
    void checkingForCorrectnessOfReturnedHistoryList() throws ManagerSaveException{
        Task task = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task);
        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        taskManager.createEpicTask(epicTask);
        SubTask subTask = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask);

        taskManager.getTaskById(task.getId());
        taskManager.getEpicTaskById(epicTask.getId());
        taskManager.getSubTaskById(subTask.getId());

        assertEquals(taskManager.getHistory().size(), 3);
        assertEquals(taskManager.getHistory().get(0), task);
        assertEquals(taskManager.getHistory().get(1), epicTask);
        assertEquals(taskManager.getHistory().get(2), subTask);

        taskManager.getTaskById(task.getId());

        assertEquals(taskManager.getHistory().size(), 3);
        assertEquals(taskManager.getHistory().get(0), epicTask);
        assertEquals(taskManager.getHistory().get(1), subTask);
        assertEquals(taskManager.getHistory().get(2), task);
    }

    @Test
    void checkingForCorrectnessOfReturnedSubtaskFromConnectedEpic() throws ManagerSaveException{
        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        taskManager.createEpicTask(epicTask);
        SubTask subTask1 = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        assertEquals(taskManager.getAllSubTasks().size(), 2);
        assertEquals(taskManager.getAllEpicTasks().size(), 1);

        assertEquals(taskManager.getSubTaskOfACertainEpicTask(epicTask.getId()).size(), 2);
        assertEquals(taskManager.getSubTaskOfACertainEpicTask(epicTask.getId()).get(0), subTask1);
    }

    @Test
    void CheckingCorrectnessOfReturnedListOfAllTasks() throws ManagerSaveException{
        Task task1 = new Task("Task", "Description",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        Task task2 = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        Task task3 = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        assertEquals(taskManager.getAllTasks().size(), 3);
        assertEquals(taskManager.getAllTasks().get(0), task1);
    }

    @Test
    void CheckingCorrectnessOfReturnedListOfAllEpicTasks() throws ManagerSaveException{
        EpicTask epicTask1 = new EpicTask("EpicTask", "Description");
        EpicTask epicTask2 = new EpicTask("EpicTask", "Description");
        EpicTask epicTask3 = new EpicTask("EpicTask", "Description");
        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);
        taskManager.createEpicTask(epicTask3);

        assertEquals(taskManager.getAllEpicTasks().size(), 3);
        assertEquals(taskManager.getAllEpicTasks().get(0), epicTask1);
    }

    @Test
    void CheckingCorrectnessOfReturnedListOfAllSubTasks() throws ManagerSaveException{
        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        taskManager.createEpicTask(epicTask);
        SubTask subTask1 = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        SubTask subTask3 = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        assertEquals(taskManager.getAllSubTasks().size(), 3);
        assertEquals(taskManager.getAllSubTasks().get(0), subTask1);
    }

    @Test
    void checkingForCorrectnessOfReturnedTaskOfAnyTypeByItsId() throws ManagerSaveException{
        Task task = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task);
        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        taskManager.createEpicTask(epicTask);
        SubTask subTask = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask);

        assertEquals(taskManager.findTask(1), task);
    }

    @Test
    void CheckingForCorrectnessOfExceptionOutputAtTimeIntersection() throws ManagerSaveException{
        Task task = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task);
       final IOException exception = assertThrows(IOException.class,
               () -> taskManager.createTask(new Task("Task", "Description",
                       LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15))));
        assertEquals(exception.getMessage(), "Время задач не может пересекаться.");
    }
}