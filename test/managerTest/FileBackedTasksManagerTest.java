package managerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksTest.EpicTask;
import tasksTest.SubTask;
import tasksTest.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends AbstractTaskManagerTest<FileBackedTasksManager>{
    @BeforeEach
    public void setUp() {
        File file = new File("test/resourcesTest/dataMemoryTest.csv");
        taskManager = new FileBackedTasksManager(file);
    }

    @Test
    public void checkingCorrectnessOfSaving() throws IOException {
        File fileTest = new File("test/resourcesTest/dataMemoryTest.csv");
        File correctFile = new File("test/resourcesTest/dataMemoryTest.csv");
        taskManager = new FileBackedTasksManager(fileTest);
        Task task1 = new Task("Task1", "Des",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        Task task2 = new Task("Task2", "Des",
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(30));
        Task task3 = new Task("Task3", "Des",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(45));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        EpicTask epicTask1 = new EpicTask("EpicTask1", "Des");
        EpicTask epicTask2 = new EpicTask("EpicTask2", "Des");
        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);
        SubTask subTask1 = new SubTask("SubTask1", "Des", epicTask1.getId(),
                LocalDateTime.of(2023, 10, 10, 10, 0), Duration.ofMinutes(15));
        SubTask subTask2 = new SubTask("SubTask2", "Des", epicTask1.getId(),
                LocalDateTime.of(2024, 10, 10, 10, 0), Duration.ofMinutes(30));
        SubTask subTask3 = new SubTask("SubTask3", "Des", epicTask2.getId(),
                LocalDateTime.of(2020, 11, 10, 10, 0), Duration.ofMinutes(60));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        String correctStringTaskTest = Files.readString(correctFile.toPath());
        String stringTaskTest = Files.readString(fileTest.toPath());
        assertEquals(correctStringTaskTest, stringTaskTest);
    }

    @Test
    public void checkingCorrectnessOfLoad() throws ManagerSaveException {
        File file = new File("test/resourcesTest/dataMemoryTestForLoad.csv");
        FileBackedTasksManager taskManager = FileBackedTasksManager.load(file);

        assertEquals(taskManager.getAllTasks().size(), 3);
    }
}