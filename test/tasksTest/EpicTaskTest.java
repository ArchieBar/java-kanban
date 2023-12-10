package tasksTest;

import main.manager.InMemoryTaskManager;
import main.manager.ManagerSaveException;
import org.junit.jupiter.api.Test;
import main.tasks.EpicTask;
import main.tasks.Status;
import main.tasks.SubTask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {
    @Test
    public void checkingTheStatusWithAnEmptyListOfSubtasks() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        EpicTask epicTask = new EpicTask("Epic", "Description");
        taskManager.createEpicTask(epicTask);
        assertEquals(Status.NEW, epicTask.getStatus());

        taskManager.createSubTask(
                new SubTask("SubTask", "Description", epicTask.getId(), Status.IN_PROGRESS,
                        LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15)));
        assertEquals(Status.IN_PROGRESS, epicTask.getStatus());

        taskManager.removeAllSubTask();
        assertEquals(Status.NEW, epicTask.getStatus());
    }

    @Test
    public void checkingTheStatusOfTheEpicWithTheStatusOfAllSubtasksNew() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        EpicTask epicTask = new EpicTask("Epic", "Description");
        taskManager.createEpicTask(epicTask);
        assertEquals(Status.NEW, epicTask.getStatus());

        taskManager.createSubTask(new SubTask("SubTask1", "Description", epicTask.getId(),
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15)));
        taskManager.createSubTask(new SubTask("SubTask2", "Description", epicTask.getId(),
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15)));
        taskManager.createSubTask(new SubTask("SubTask3", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15)));
        assertEquals(Status.NEW, epicTask.getStatus());
    }

    @Test
    public void checkingTheStatusOfTheEpicWithTheStatusOfAllSubtasksDone() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        EpicTask epicTask = new EpicTask("Epic", "Description");
        taskManager.createEpicTask(epicTask);
        assertEquals(Status.NEW, epicTask.getStatus());

        taskManager.createSubTask(new SubTask("SubTask1", "Description", epicTask.getId(), Status.DONE,
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15)));
        taskManager.createSubTask(new SubTask("SubTask2", "Description", epicTask.getId(), Status.DONE,
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15)));
        taskManager.createSubTask(new SubTask("SubTask3", "Description", epicTask.getId(), Status.DONE,
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15)));
        assertEquals(Status.DONE, epicTask.getStatus());
    }

    @Test
    public void checkingTheStatusOfTheEpicWithTheStatusOfSubtasksNewAndDone() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        EpicTask epicTask = new EpicTask("Epic", "Description");
        taskManager.createEpicTask(epicTask);
        assertEquals(Status.NEW, epicTask.getStatus());

        taskManager.createSubTask(new SubTask("SubTask1", "Description", epicTask.getId(), Status.NEW,
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15)));
        taskManager.createSubTask(new SubTask("SubTask2", "Description", epicTask.getId(), Status.NEW,
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15)));
        taskManager.createSubTask(new SubTask("SubTask3", "Description", epicTask.getId(), Status.DONE,
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15)));
        assertEquals(Status.IN_PROGRESS, epicTask.getStatus());
    }

    @Test
    public void checkingTheStatusOfTheEpicWithTheStatusOfAllSubtasksInProgress() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        EpicTask epicTask = new EpicTask("Epic", "Description");
        taskManager.createEpicTask(epicTask);
        assertEquals(Status.NEW, epicTask.getStatus());

        taskManager.createSubTask(new SubTask("SubTask1", "Description", epicTask.getId(), Status.IN_PROGRESS,
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15)));
        taskManager.createSubTask(new SubTask("SubTask2", "Description", epicTask.getId(), Status.IN_PROGRESS,
                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(15)));
        taskManager.createSubTask(new SubTask("SubTask3", "Description", epicTask.getId(), Status.IN_PROGRESS,
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15)));
        assertEquals(Status.IN_PROGRESS, epicTask.getStatus());
    }
}