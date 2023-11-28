package tasksTest;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final static Type TYPE = Type.SUBTASK;
    private int idEpicTask;

    public SubTask(String name, String description, int idEpicTask, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.idEpicTask = idEpicTask;
    }

    public SubTask(String name, String description, int idEpicTask, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.idEpicTask = idEpicTask;
    }

    public SubTask(String name, String description, Status status, int idEpicTask,
                   LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.idEpicTask = idEpicTask;
    }

    @Override
    public String toString() {
        return "Название: " + super.getName() + ", "
                + "Описание: " + super.getDescription() + ", "
                + "Статус: " + super.getStatus() + ", "
                + "ID: " + super.getId() + ", "
                + "EpicID: " + idEpicTask;
    }
    @Override
    public Type getType() {
        return TYPE;
    }

    public int getIdEpicTask() {
        return idEpicTask;
    }

    public void setIdEpicTask(int idEpicTask) {
        this.idEpicTask = idEpicTask;
    }
}
