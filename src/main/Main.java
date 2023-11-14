package main;

import manager.FileBackedTasksManager;
import manager.Manager;
import tasks.Status;
import manager.TaskManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        //TaskManager taskManager = Manager.getDefault();
        FileBackedTasksManager taskManager = new FileBackedTasksManager();

        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        taskManager.createTask(task2);

        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание 1", Status.NEW);
        taskManager.createEpicTask(epicTask1);
        SubTask subTask1_1 = new SubTask("Подзадача 1-1", "Описание 1-1", Status.NEW,  3);
        taskManager.createSubTask(subTask1_1);
        SubTask subTask2_1 = new SubTask("Подзадача 2-1", "Описание 2-1", Status.NEW,  3);
        taskManager.createSubTask(subTask2_1);
        SubTask subTask3_1 = new SubTask("Подзадача 3-1", "Описание 3-1", Status.NEW,  3);
        taskManager.createSubTask(subTask3_1);

        EpicTask epicTask2 = new EpicTask("Эпик 2", "Описание 2", Status.NEW);
        taskManager.createEpicTask(epicTask2);

        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getEpicTaskById(3));
        System.out.println(taskManager.getSubTaskById(4));
        System.out.println(taskManager.getEpicTaskById(7));

        System.out.println(taskManager.getHistory());

        taskManager.removeAllTask();

        System.out.println(taskManager.getHistory());

        System.out.println(taskManager.getEpicTaskById(7));

        System.out.println(taskManager.getHistory());

        System.out.println(task1.getClass());
        try {
            System.out.println(task1.getClass() == Class.forName("tasks.Task"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
