package manager;

import csv.CsvFormat;
import org.w3c.dom.ls.LSOutput;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.Type;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;



public class FileBackedTasksManager extends InMemoryTaskManager {
    private void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("dataStorage.csv"))){
            fileWriter.write("id,type,name,status,description,epic_id,[subtask_id]");
            fileWriter.newLine();
            for (final Task task : super.getAllTasks()) {
                fileWriter.write(CsvFormat.taskToString(task));
                fileWriter.newLine();
            }
            for (final EpicTask epicTask : super.getAllEpicTasks()) {
                fileWriter.write(CsvFormat.taskToString(epicTask));
                fileWriter.newLine();
            }
            for (final SubTask subTask : super.getAllSubTasks()) {
                fileWriter.write(CsvFormat.taskToString(subTask));
                fileWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {

    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        super.createEpicTask(epicTask);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask newEpicTask) {
        super.updateEpicTask(newEpicTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        super.updateSubTask(newSubTask);
        save();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpicTask() {
        super.removeAllEpicTask();
        save();
    }

    @Override
    public void removeAllSubTask() {
     super.removeAllSubTask();
     save();
    }

    @Override
    public void removeTaskById(Integer idTask) {
        super.removeTaskById(idTask);
        save();
    }

    @Override
    public void removeEpicTaskById(Integer idEpicTask) {
        super.removeEpicTaskById(idEpicTask);
        save();
    }

    @Override
    public void removeSubTaskById(Integer idSubTask) {
        super.removeSubTaskById(idSubTask);
        save();
    }
}
