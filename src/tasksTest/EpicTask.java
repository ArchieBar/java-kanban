package tasksTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicTask extends Task {
    private final static Type TYPE = Type.EPICTASK;
    private ArrayList<Integer> subTasksIds = new ArrayList<>();
    private LocalDateTime endTime;

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public EpicTask(String name, String description, Status status,
                    LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.endTime = startTime.plus(duration);
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
        return TYPE;
    }

    public void setSubTasksIds(int subTasksIds) {
        this.subTasksIds.add(subTasksIds);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public Duration getDuration() {
        if (startTime != null && endTime != null) {
            return Duration.between(startTime, endTime);
        } else return null;

    }
}
