package tasksTest;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    private final static Type TYPE = Type.TASK;
    private String name;
    private String description;
    private Status status;
    protected Duration duration;
    protected LocalDateTime startTime = null;
    private int id = -1;

    protected Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = LocalDateTime.now();
    }

    protected Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }
    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Type getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "Название: " + name + ", "
                + "Описание: " + description + ", "
                + "Статус: " + status + ", "
                + "ID: " + id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
            return startTime;
    }

    public Duration getDuration() {
            return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
