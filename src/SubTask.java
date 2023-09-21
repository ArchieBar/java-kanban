public class SubTask extends Task {
    private int idEpicTask;
    public SubTask(String name, String description, int idEpicTask) {
        super(name, description);
        this.idEpicTask = idEpicTask;
    }

    public int getIdEpicTask() {
        return idEpicTask;
    }
}
