package managerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasksTest.EpicTask;
import tasksTest.SubTask;
import tasksTest.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    public void testAddHistoryShouldAddToHistoryWhenTaskNotNull() throws ManagerSaveException {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task", "Description",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        task.setId(1);
        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        epicTask.setId(2);
        SubTask subTask = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        subTask.setId(3);

        historyManager.addHistory(task);
        historyManager.addHistory(epicTask);
        historyManager.addHistory(subTask);

        int actualArea = historyManager.getHistory().size();
        int expectedArea = 3;
        assertEquals(expectedArea, actualArea);

        Task actualTask = historyManager.getHistory().get(0);
        Task expectedTask = task;
        assertEquals(expectedTask, actualTask);
    }

    @Test
    public void testAddHistoryDuplicateTaskShouldNotBeAtEndOfListAgainIfTaskIsNotNull() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task = new Task("Task", "Description",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        task.setId(1);
        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        epicTask.setId(2);
        SubTask subTask = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        subTask.setId(3);

        historyManager.addHistory(task);
        historyManager.addHistory(epicTask);
        historyManager.addHistory(subTask);
        historyManager.addHistory(task);

        int actualSize = historyManager.getHistory().size();
        int expectedSize = 3;
        assertEquals(expectedSize, actualSize);

        Task actualTask = historyManager.getHistory().get(2);
        Task expectedTask = task;
        assertEquals(expectedTask, actualTask);
    }

    @Test
    public void testAddHistoryAnErrorShouldNotOccurWhenTaskIsNullAndHistoryListShouldNotChange() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task = new Task("Task", "Description",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        task.setId(1);
        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        epicTask.setId(2);
        SubTask subTask = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        subTask.setId(3);

        historyManager.addHistory(task);
        historyManager.addHistory(epicTask);
        historyManager.addHistory(subTask);
        historyManager.addHistory(null);

        int actualSize = historyManager.getHistory().size();
        int expectedSize = 3;
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testRemoveHistoryWhenDeletingAHistoryFromBeginningOfMiddleOfEndOfList() throws ManagerSaveException {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task", "Description",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        task1.setId(1);
        Task task2 = new Task("Task", "Description",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        task2.setId(2);
        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        epicTask.setId(3);
        SubTask subTask = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        subTask.setId(4);

        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        historyManager.addHistory(epicTask);
        historyManager.addHistory(subTask);

        int actualSize = historyManager.getHistory().size();
        int expectedSize = 4;
        assertEquals(expectedSize, actualSize);

        historyManager.remove(task2.getId());
        actualSize = historyManager.getHistory().size();
        expectedSize = 3;
        assertEquals(expectedSize, actualSize);

        historyManager.remove(subTask.getId());
        actualSize = historyManager.getHistory().size();
        expectedSize = 2;
        assertEquals(expectedSize, actualSize);

        historyManager.remove(task1.getId());
        actualSize = historyManager.getHistory().size();
        expectedSize = 1;
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testRemoveHistoryWhenDeletingAHistoryByANonExistentID() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task = new Task("Task", "Description",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
        task.setId(1);
        EpicTask epicTask = new EpicTask("EpicTask", "Description");
        epicTask.setId(2);
        SubTask subTask = new SubTask("SubTask", "Description", epicTask.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        subTask.setId(3);

        historyManager.addHistory(task);
        historyManager.addHistory(epicTask);
        historyManager.addHistory(subTask);

        int actualSize = historyManager.getHistory().size();
        int expectedSize = 3;
        assertEquals(expectedSize, actualSize);

        historyManager.remove(-1);
        actualSize = historyManager.getHistory().size();
        expectedSize = 3;
        assertEquals(expectedSize, actualSize);
    }
}