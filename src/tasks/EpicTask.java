package tasks;

import java.util.ArrayList;

public class EpicTask extends Task {
    private final Type type = Type.EPICTASK;
    private ArrayList<Integer> subTasksIds = new ArrayList<>();
    public EpicTask(String name, String description, Status status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        return "Название: " + super.getName() + ", "
                + "Описание: " + super.getDescription() + ", "
                + "Статус: " + super.getStatus() + ", "
                + "ID: " + super.getId() + ", "
                + "SubTaskID: " + subTasksIds;
    }

    @Override
    public Type getType() {
        return type;
    }
    public void setSubTasksIdsFromCsv(ArrayList<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    public void setSubTasksIds(int subTasksIds) {
        this.subTasksIds.add(subTasksIds);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }
    public String getSubIdsString() {
        if (subTasksIds.isEmpty()) {
            return null;
        }
        StringBuilder subIdString = new StringBuilder();
        for (int id : subTasksIds) {
            subIdString.append(" " + id);
        }
        return subIdString.toString().trim();
    }
}
