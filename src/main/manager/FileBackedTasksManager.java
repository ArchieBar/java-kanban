package main.manager;

import main.csv.CsvFormat;
import main.tasks.Task;
import main.tasks.EpicTask;
import main.tasks.SubTask;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws ManagerSaveException {
        final FileBackedTasksManager taskManager = Manager.getFileBackedTasksManager();
        System.out.println(taskManager);
    }

    /**
     * Метод по сохранению всех задач и истории просмотов в файл формата CSV
     * Вызывается при создании, удалении, изменении и получению задач
     */
    protected void save() throws ManagerSaveException, IOException {
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
            throw new ManagerSaveException("Ошибка сохранения файла: " + file.getName());
        }
    }

    /**
     * Метод по загрузке всех задач и истории просмотров, а так же последнего установленного id из файла формата CSV.
     * В конце метода вызывается сохранение всех загруженных данных в файл формата CSV
     */
    public FileBackedTasksManager load() throws IOException, InterruptedException {
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
            throw new ManagerSaveException("Ошибка загрузки файла: " + file.getName());
        }

        return taskManager;
    }

    @Override
    public void createTask(Task task) throws IOException {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) throws IOException {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) throws IOException {
        super.createEpicTask(epicTask);
        save();
    }

    @Override
    public void updateTask(Task newTask, Task oldTask) throws IOException {
        super.updateTask(newTask, oldTask);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask newEpicTask, EpicTask oldEpicTask) throws IOException {
        super.updateEpicTask(newEpicTask, oldEpicTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask, SubTask oldSubTask) throws IOException {
        super.updateSubTask(newSubTask, oldSubTask);
        save();
    }

    @Override
    public void removeAllTask() throws IOException {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpicTask() throws IOException {
        super.removeAllEpicTask();
        save();
    }

    @Override
    public void removeAllSubTask() throws IOException {
        super.removeAllSubTask();
        save();
    }

    @Override
    public void removeTaskById(Integer idTask) throws IOException {
        super.removeTaskById(idTask);
        save();
    }

    @Override
    public void removeEpicTaskById(Integer idEpicTask) throws IOException {
        super.removeEpicTaskById(idEpicTask);
        save();
    }

    @Override
    public void removeSubTaskById(Integer idSubTask) throws IOException {
        super.removeSubTaskById(idSubTask);
        save();
    }

    @Override
    public Task getTaskById(Integer taskId) throws IOException {
        historyManager.addHistory(allTasks.get(taskId));
        save();
        return allTasks.get(taskId);
    }

    @Override
    public EpicTask getEpicTaskById(Integer epicTaskId) throws IOException {
        historyManager.addHistory(allEpicTasks.get(epicTaskId));
        save();
        return allEpicTasks.get(epicTaskId);
    }

    @Override
    public SubTask getSubTaskById(Integer subTaskId) throws IOException {
        historyManager.addHistory(allSubTasks.get(subTaskId));
        save();
        return allSubTasks.get(subTaskId);
    }

}
