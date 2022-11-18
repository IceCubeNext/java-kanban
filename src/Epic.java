import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    Epic(int id, String name, String description, ArrayList<Subtask> subtasks){
        super(id, name, description);
        this.subtasks = subtasks;
    }

    Epic(String name, String description, ArrayList<Subtask> subtasks){
        super(name, description);
        this.subtasks = subtasks;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public boolean deleteSubtask(Subtask subtask){
        if (this.subtasks.contains(subtask)){
            this.subtasks.remove(subtask);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Epic epic = (Epic) obj;
        return epic.getId() == this.getId()
                && Objects.equals(epic.getName(), this.getName())
                && Objects.equals(epic.getDescription(), this.getDescription())
                && Objects.equals(epic.getStatus(), this.getStatus())
                && Objects.equals(epic.getSubtasks(), this.getSubtasks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getName(), this.getDescription(),
                this.getStatus(), this.getSubtasks());
    }

    @Override
    public String toString() {
        String result =  "Epic{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() +'\'' +
                ", status='" + this.getStatus() + '\'';
        if (this.getSubtasks() != null) {
            result += ", subtasks.size='" + this.getSubtasks().size();
        } else {
            result += ", subtasks.size=null";
        }
        return result + '}';
    }
}
