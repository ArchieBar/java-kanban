package managerTest;

import main.http.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.tasks.*;
import main.KVServer;
import main.manager.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class HttpTaskManagerTest extends AbstractTaskManagerTest<HttpTaskManager> {

    private KVServer kvServer;


    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = Manager.getHttpTaskManager();
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    public void taskManagerShouldThrowExceptionWithTwoIntersectionTasks() throws IOException {
        Task task = new Task("Task", "Description",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createTask(task);
        final IOException exception = assertThrows(IOException.class,
                () -> taskManager.createTask(new Task("Task", "Description",
                        LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15))));
        assertEquals(exception.getMessage(), "Время задач не может пересекаться.");
    }

    @Test
    public void testLoadAndSaveWithThreeTasks() throws IOException, InterruptedException {
        HttpTaskManager secondTaskManager;
        Task task = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        EpicTask epicTask = new EpicTask("Task", "Description", 2);
        SubTask subTask = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2023, 10, 10, 10, 0),Duration.ofMinutes(15));
        taskManager.createTask(task);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);

        taskManager.load();
        secondTaskManager = taskManager;

        assertEquals(1, secondTaskManager.getAllTasks().size());
        assertEquals(1, secondTaskManager.getAllEpicTasks().size());
        assertEquals(1, secondTaskManager.getAllSubTasks().size());
        assertEquals(0, secondTaskManager.getHistory().size());
    }

    @Test
    public void testLoadEmptyData() throws IOException, InterruptedException {
        taskManager.load();

        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void testRemoveAllTasks() throws IOException {
        HttpTaskManager secondTaskManager;
        Task task = new Task("Task", "Description",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        EpicTask epicTask = new EpicTask("Task", "Description", 2);
        SubTask subTask = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2023, 10, 10, 10, 0),Duration.ofMinutes(15));

        taskManager.createTask(task);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);

        taskManager.removeAllTask();

        assertEquals(0, taskManager.getAllTasks().size());
        assertEquals(0, taskManager.getHistory().size());
    }
}