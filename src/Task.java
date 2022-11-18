import java.util.Objects;

public class Task {
    int id;
    String name;
    String description;
    Status status;

    Task(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Task task = (Task) obj;
        return task.id == this.id
                && Objects.equals(task.name, this.name)
                && Objects.equals(task.description, this.description)
                && Objects.equals(task.status, this.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.description, this.status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + this.id + '\'' +
                ", name='" + this.name + '\'' +
                ", description='" + this.description +'\'' +
                ", status='" + this.status + '\'' + '}';
    }
}
