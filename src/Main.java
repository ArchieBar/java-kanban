public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Задача 1", "Описание 1");
        manager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2");
        manager.createTask(task2);

        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание 1");
        manager.createTask(epicTask1);
        SubTask subTask1_1 = new SubTask("Подзадача 1-1", "Описание 1-1", 3);
        manager.createTask(subTask1_1);
        SubTask subTask2_1 = new SubTask("Подзадача 2-1", "Описание 2-1", 3);
        manager.createTask(subTask2_1);

        EpicTask epicTask2 = new EpicTask("Эпик 2", "Описание 2");
        manager.createTask(epicTask2);
        SubTask subTask1_2 = new SubTask("Подзадача 1_2", "Описание 1_2", 6);
        manager.createTask(subTask1_2);

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpicTask());
        System.out.println(manager.getAllSubTask());

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicTaskById(3));
        System.out.println(manager.getSubTaskById(4));

        System.out.println(manager.getSubTaskOfACertainEpicTask(3));

        manager.updateSubTask(subTask1_1, "DONE",4);
        manager.updateSubTask(subTask1_2, "DONE", 7);

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpicTask());
        System.out.println(manager.getAllSubTask());

        manager.removeAllSubTask();

        manager.getAllEpicTask();
    }
}
