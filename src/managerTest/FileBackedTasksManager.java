package managerTest;

import csv.CsvFormat;
import tasksTest.*;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws ManagerSaveException {
        final FileBackedTasksManager taskManager = Manager.getFileBackedTasksManager();
//        Task task1 = new Task("Task1", "Des",
//                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofMinutes(15));
//        Task task2 = new Task("Task2", "Des",
//                LocalDateTime.of(2021, 10, 10, 10, 0), Duration.ofMinutes(30));
//        Task task3 = new Task("Task3", "Des",
//                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(45));
//        taskManager.createTask(task1);
//        taskManager.createTask(task2);
//        taskManager.createTask(task3);
//
//        EpicTask epicTask1 = new EpicTask("EpicTask1", "Des");
//        EpicTask epicTask2 = new EpicTask("EpicTask2", "Des");
//        taskManager.createEpicTask(epicTask1);
//        taskManager.createEpicTask(epicTask2);
//
//        SubTask subTask1 = new SubTask("SubTask1", "Des", epicTask1.getId(),
//                LocalDateTime.of(2023, 10, 10, 10, 0), Duration.ofMinutes(15));
//        SubTask subTask2 = new SubTask("SubTask2", "Des", epicTask1.getId(),
//                LocalDateTime.of(2024, 10, 10, 10, 0), Duration.ofMinutes(30));
//        SubTask subTask3 = new SubTask("SubTask3", "Des", epicTask2.getId(),
//                LocalDateTime.of(2020, 11, 10, 10, 0), Duration.ofMinutes(60));
//        taskManager.createSubTask(subTask1);
//        taskManager.createSubTask(subTask2);
//        taskManager.createSubTask(subTask3);

        System.out.println(taskManager);
    }

    /**
     * Метод по сохранению всех задач и истории просмотов в файл формата CSV
     * Вызывается при создании, удалении, изменении и получению задач
     */
    private void save() throws ManagerSaveException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(CsvFormat.getHeadCSV());
            bufferedWriter.newLine();

            for (final Task task : super.getAllTasks()) {
                bufferedWriter.write(CsvFormat.taskToString(task));
                bufferedWriter.newLine();
            }

            for (final EpicTask epicTask : super.getAllEpicTasks()) {
                bufferedWriter.write(CsvFormat.taskToString(epicTask));
                bufferedWriter.newLine();
            }

            for (final SubTask subTask : super.getAllSubTasks()) {
                bufferedWriter.write(CsvFormat.taskToString(subTask));
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();
            bufferedWriter.write(CsvFormat.historyToString(historyManager));
            bufferedWriter.newLine();

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла: " + file.getName(), e);
        }
    }

    /**
     * Метод по загрузке всех задач и истории просмотров, а так же последнего установленного id из файла формата CSV.
     * В конце метода вызывается сохранение всех загруженных данных в файл формата CSV
     */
    public static FileBackedTasksManager load(File file) throws ManagerSaveException {
        final FileBackedTasksManager taskManager = new FileBackedTasksManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] line = csv.split(System.lineSeparator());
            int maxId = -1;
            for (int i = 1; i < line.length; i++) {
                if (line[i].isBlank()) {
                    break;
                }
                Task task = CsvFormat.taskFromString(line[i]);
                if (task == null) {
                    break;
                }
                int id = task.getId();
                if (id > maxId) {
                    maxId = id;
                }
                switch (task.getType()) {
                    case TASK:
                        taskManager.allTasks.put(id, task);
                        break;
                    case SUBTASK:
                        taskManager.allSubTasks.put(id, (SubTask) task);
                        break;
                    case EPICTASK:
                        taskManager.allEpicTasks.put(id, (EpicTask) task);
                }
            }

            for (SubTask subTask : taskManager.getAllSubTasks()) {
                int idEpic = subTask.getIdEpicTask();
                EpicTask epicTask = (EpicTask) taskManager.findTask(idEpic);
                epicTask.setSubTasksIds(subTask.getId());
            }

            if (!line[line.length - 1].equals(line[0])) {
                ArrayList<Integer> historyId = CsvFormat.historyFromString(line[line.length - 1]);
                if (!(historyId == null)) {
                    for (int id : historyId) {
                        taskManager.historyManager.addHistory(taskManager.findTask(id));
                    }
                }
            }
            taskManager.setId(Math.max(maxId, 0));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки файла: " + file.getName(), e);
        }

        return taskManager;
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) throws ManagerSaveException {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) throws ManagerSaveException {
        super.createEpicTask(epicTask);
        save();
    }

    @Override
    public void updateTask(Task newTask, Task oldTask) throws ManagerSaveException {
        super.updateTask(newTask, oldTask);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask newEpicTask, EpicTask oldEpicTask) throws ManagerSaveException {
        super.updateEpicTask(newEpicTask, oldEpicTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask, SubTask oldSubTask) throws ManagerSaveException {
        super.updateSubTask(newSubTask, oldSubTask);
        save();
    }

    @Override
    public void removeAllTask() throws ManagerSaveException {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpicTask() throws ManagerSaveException {
        super.removeAllEpicTask();
        save();
    }

    @Override
    public void removeAllSubTask() throws ManagerSaveException {
        super.removeAllSubTask();
        save();
    }

    @Override
    public void removeTaskById(Integer idTask) throws ManagerSaveException {
        super.removeTaskById(idTask);
        save();
    }

    @Override
    public void removeEpicTaskById(Integer idEpicTask) throws ManagerSaveException {
        super.removeEpicTaskById(idEpicTask);
        save();
    }

    @Override
    public void removeSubTaskById(Integer idSubTask) throws ManagerSaveException {
        super.removeSubTaskById(idSubTask);
        save();
    }

    @Override
    public Task getTaskById(int taskId) throws ManagerSaveException {
        historyManager.addHistory(allTasks.get(taskId));
        save();
        return allTasks.get(taskId);
    }

    @Override
    public EpicTask getEpicTaskById(int epicTaskId) throws ManagerSaveException {
        historyManager.addHistory(allEpicTasks.get(epicTaskId));
        save();
        return allEpicTasks.get(epicTaskId);
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) throws ManagerSaveException {
        historyManager.addHistory(allSubTasks.get(subTaskId));
        save();
        return allSubTasks.get(subTaskId);
    }

}
