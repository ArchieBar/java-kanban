import java.util.ArrayList;
public class EpicTask extends Task{
    private ArrayList<Integer> idSubTask = new ArrayList<>();
    public EpicTask(String name, String description) {
        super(name, description);
    }

    public void setIdSubTask(int idSubTask) {
        this.idSubTask.add(idSubTask);
    }

    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }
}
