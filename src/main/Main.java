package main;

import managerTest.Manager;
import managerTest.ManagerSaveException;
import managerTest.TaskManager;
import tasksTest.EpicTask;
import tasksTest.SubTask;
import tasksTest.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        TaskManager taskManager = Manager.getDefault();

        Task task1 = new Task("Задача 1", "Описание 1",
                LocalDateTime.of(2020, 10, 10, 10, 0),Duration.ofMinutes(15));
        taskManager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2",
                LocalDateTime.of(2020, 10, 10, 10, 10), Duration.ofMinutes(15));
        taskManager.createTask(task2);

        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание 1");
        taskManager.createEpicTask(epicTask1);
        SubTask subTask1_1 = new SubTask("Подзадача 1-1", "Описание 1-1",  epicTask1.getId(),
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask1_1);
        SubTask subTask2_1 = new SubTask("Подзадача 2-1", "Описание 2-1",  epicTask1.getId(),
                LocalDateTime.of(2023, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask2_1);
        SubTask subTask3_1 = new SubTask("Подзадача 3-1", "Описание 3-1",  epicTask1.getId(),
                LocalDateTime.of(2024, 10, 10, 10, 0), Duration.ofMinutes(15));
        taskManager.createSubTask(subTask3_1);

        EpicTask epicTask2 = new EpicTask("Эпик 2", "Описание 2");
        taskManager.createEpicTask(epicTask2);

        System.out.println("Tasks: " + taskManager.getPrioritizedTasks());
    }
}
