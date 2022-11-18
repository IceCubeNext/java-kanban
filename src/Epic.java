import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();
    Epic(int id, String name, String description, ArrayList<Subtask> subtasks){
        super(id, name, description);
        this.subtasks = subtasks;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Epic epic = (Epic) obj;
        return epic.id == this.id
                && Objects.equals(epic.name, this.name)
                && Objects.equals(epic.description, this.description)
                && Objects.equals(epic.status, this.status)
                && Objects.equals(epic.subtasks, this.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.description, this.status, this.subtasks);
    }

    @Override
    public String toString() {
        String result =  "Epic{" +
                "id='" + this.id + '\'' +
                ", name='" + this.name + '\'' +
                ", description='" + this.description +'\'' +
                ", status='" + this.status + '\'';
        if (this.subtasks != null) {
            result += ", subtasks.size='" + this.subtasks.size();
        } else {
            result += ", subtasks.size=null";
        }
        return result + '}';
    }
}
