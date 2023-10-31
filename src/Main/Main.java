package Main;

import Manager.Manager;
import Manager.Status;
import Manager.TaskManager;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefault();

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

        taskManager.removeAllTask();

        //System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getEpicTaskById(7));

        System.out.println(taskManager.getHistory());

        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getEpicTaskById(7));

        System.out.println(taskManager.getHistory());


    }
}
