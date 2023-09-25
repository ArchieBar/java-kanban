public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Задача 1", "Описание 1", "NEW");
        manager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", "NEW");
        manager.createTask(task2);

        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание 1", "NEW");
        manager.createEpicTask(epicTask1);
        SubTask subTask1_1 = new SubTask("Подзадача 1-1", "Описание 1-1", "NEW",  3);
        manager.createSubTask(subTask1_1);
        SubTask subTask2_1 = new SubTask("Подзадача 2-1", "Описание 2-1", "NEW",  3);
        manager.createSubTask(subTask2_1);

        EpicTask epicTask2 = new EpicTask("Эпик 2", "Описание 2", "NEW");
        manager.createEpicTask(epicTask2);
        SubTask subTask1_2 = new SubTask("Подзадача 1_2", "Описание 1_2", "NEW",  6);
        manager.createSubTask(subTask1_2);

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpicTask());
        System.out.println(manager.getAllSubTask());

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicTaskById(3));
        System.out.println(manager.getSubTaskById(4));

        System.out.println(manager.getSubTaskOfACertainEpicTask(3));

        subTask1_1.setStatus("DONE");
        manager.updateSubTask(subTask1_1);

        subTask1_2.setStatus("DONE");
        manager.updateSubTask(subTask1_2);

        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpicTask());
        System.out.println(manager.getAllSubTask());

        manager.removeAllSubTask();

        manager.getAllEpicTask();
    }
}
