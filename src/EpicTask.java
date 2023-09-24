import java.util.ArrayList;
public class EpicTask extends Task{
    private ArrayList<Integer> idSubTask = new ArrayList<>();
    public EpicTask(String name, String description) {
        super(name, description);
    }


    @Override
    public String toString() {
        return "Название: " + super.getName() + ", "
                + "Описание: " + super.getDescription() + ", "
                + "Статус: " + super.getStatus() + ", "
                + "ID: " + super.getId() + ", "
                + "SubTaskID: " + idSubTask;
    }
    public void setIdSubTask(int idSubTask) {
        this.idSubTask.add(idSubTask);
    }

    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }
}
