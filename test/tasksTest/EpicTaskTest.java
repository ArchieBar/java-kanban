package tasksTest;

import managerTest.InMemoryTaskManager;
import managerTest.ManagerSaveException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {
    @Test
    public void checkingTheStatusWithAnEmptyListOfSubtasks() throws ManagerSaveException {
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
    public void checkingTheStatusOfTheEpicWithTheStatusOfAllSubtasksNew() throws ManagerSaveException {
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
    public void checkingTheStatusOfTheEpicWithTheStatusOfAllSubtasksDone() throws ManagerSaveException {
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
    public void checkingTheStatusOfTheEpicWithTheStatusOfSubtasksNewAndDone() throws ManagerSaveException {
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
    public void checkingTheStatusOfTheEpicWithTheStatusOfAllSubtasksInProgress() throws ManagerSaveException {
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