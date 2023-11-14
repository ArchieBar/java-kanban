package csv;

import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.Type;

public class CsvFormat {

    /**
     * Метод по приобразованию задачи в строку формата CSV: "id,type,name,status,description,epic_id,subtask_id"
     * @param task
     * @return Строку формата CSV
     */
    public static String taskToString(Task task) {

        try {
            if (task.getClass() == Class.forName("tasks.Task")) {
                return String.format("%s,%s,%s,%s,%s,%s,%s",
                        task.getId(),
                        Type.TASK,
                        task.getName(),
                        task.getStatus(),
                        task.getDescription(),
                        null,
                        null);
            } else if (task.getClass() == Class.forName("tasks.SubTask")) {
                SubTask subTask = (SubTask) task;
                return String.format("%s,%s,%s,%s,%s,%s,%s",
                        subTask.getId(),
                        Type.SUBTASK,
                        subTask.getName(),
                        subTask.getStatus(),
                        subTask.getDescription(),
                        subTask.getIdEpicTask(),
                        null);
            } else if (task.getClass() == Class.forName("tasks.EpicTask")) {
                EpicTask epicTask = (EpicTask) task;
                return String.format("%s,%s,%s,%s,%s,%s,%s",
                        epicTask.getId(),
                        Type.EPICTASK,
                        epicTask.getName(),
                        epicTask.getStatus(),
                        epicTask.getDescription(),
                        null,
                        epicTask.getSubIdsString());
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
