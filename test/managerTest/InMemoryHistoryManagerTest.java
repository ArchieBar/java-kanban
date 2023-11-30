package managerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksTest.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryHistoryManagerTest {

    @Test
    public void CheckingForCorrectnessOfAddingATaskToHistoryList() throws ManagerSaveException {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task", "Description",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        Task task2 = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        Task task3 = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());

        assertEquals(taskManager.getHistory().size(), 3);
    }

    @Test
    public void CheckingForCorrectnessOfDeletingATaskFromHistoryList() throws ManagerSaveException {
        TaskManager taskManager = Manager.getDefault();
        Task task1 = new Task("Task", "Description",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        Task task2 = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        Task task3 = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());

        assertEquals(taskManager.getHistory().size(), 3);

        taskManager.removeAllTask();

        assertEquals(taskManager.getHistory().size(), 0);
    }

}