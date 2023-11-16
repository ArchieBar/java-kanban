package csv;

import manager.HistoryManager;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class CsvFormat {

    /**
     * Метод для формирования нотации CSV файла
     * @return String headCSV
     */
    public static String getHeadCSV() {
        return "id,type,name,status,description,epic_id";
    }

    public static String getHistoryEmpty() {
        return "The browsing history is empty.";
    }

    /**
     * Метод по приобразованию задачи в строку формата CSV: "id,type,name,status,description,epic_id"
     * @return String task
     */
    public static String taskToString(Task task) {
        if (task.getType() == Type.SUBTASK) {
            SubTask subTask = (SubTask) task;
            return subTask.getId() + "," +
                    subTask.getType() + "," +
                    subTask.getName() + "," +
                    subTask.getStatus() + "," +
                    subTask.getDescription() + "," +
                    subTask.getIdEpicTask();
        } else {
            return task.getId() + "," +
                    task.getType() + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDescription() + "," +
                    null;
        }
    }

    /**
     * Метод для формирование задачи из строки CSV файла
     * @return Task task
     * @return null при неверном формате
     */

    public static Task taskFromString(String line) {
        if (line.isEmpty()) {
            return null;
        }

        String[] dataLine = line.split(",");

        if (Type.valueOf(dataLine[1]) == Type.TASK) {

            Task task = new Task(
                    dataLine[2],
                    dataLine[4],
                    statusFromString(dataLine[3]));
            task.setId(Integer.parseInt(dataLine[0]));

            return task;
        } else if (Type.valueOf(dataLine[1]) == Type.SUBTASK) {

            SubTask subTask = new SubTask(
                    dataLine[2],
                    dataLine[4],
                    statusFromString(dataLine[3]),
                    Integer.parseInt(dataLine[5]));
            subTask.setId(Integer.parseInt(dataLine[0]));

            return subTask;
        } else if (Type.valueOf(dataLine[1]) == Type.EPICTASK) {

            EpicTask epicTask = new EpicTask(
                    dataLine[2],
                    dataLine[4],
                    statusFromString(dataLine[3]));
            epicTask.setId(Integer.parseInt(dataLine[0]));

            return epicTask;
        }
        return null;
    }

    /**
     * Метод по преобразованию истории задач в список id задач из истории
     * @return String historyToString
     */
    public static String historyToString(HistoryManager historyManager) {
        List<Task> taskList = historyManager.getHistory();
        if (taskList.isEmpty()) {
            return null;
        }
        StringBuilder historyIdString = new StringBuilder();
        for (Task task : taskList) {
            if (historyIdString.length() == 0) {
                historyIdString.append(task.getId());
            } else historyIdString.append(",").append(task.getId());
        }
        return historyIdString.toString();
    }

    /**
     * Метод по преобразованию строки в список id задач истории
     * @return ArrayList<Integer> historyId
     */
    public static ArrayList<Integer> historyFromString(String line) {
        if (line.equalsIgnoreCase(getHistoryEmpty())){
            return null;
        }
        ArrayList<Integer> historyId = new ArrayList<>();
        String[] dataHistoryString = line.split(",");
        for (String idString : dataHistoryString) {
            historyId.add(Integer.parseInt(idString));
        }
        return historyId;
    }

    /**
     * Метод преобразования статуса списка emun из строки
     * @return Status.enum
     * @return null при неверном формате
     */
    private static Status statusFromString(String status) {
        if (status.equalsIgnoreCase("NEW")) {
            return Status.NEW;
        } else if (status.equalsIgnoreCase("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else if (status.equalsIgnoreCase("DONE")) {
            return Status.DONE;
        } else {
            // Обработать как исключение?
            return null;
        }
    }
}
