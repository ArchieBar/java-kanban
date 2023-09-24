public class SubTask extends Task {
    private int idEpicTask;
    public SubTask(String name, String description, int idEpicTask) {
        super(name, description);
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

    public int getIdEpicTask() {
        return idEpicTask;
    }

    public void setIdEpicTask(int idEpicTask) {
        this.idEpicTask = idEpicTask;
    }
}
