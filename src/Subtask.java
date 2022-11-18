import java.util.Objects;

public class Subtask extends Task {
    private final int epicsId;
    Subtask(int id, int epicsId, String name, String description) {
        super(id, name, description);
        this.epicsId = epicsId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Subtask subtask = (Subtask) obj;
        return this.id == subtask.id
                && Objects.equals(this.name, subtask.name)
                && Objects.equals(this.description, subtask.description)
                && Objects.equals(this.status, subtask.status)
                && this.epicsId == subtask.epicsId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.description, this.status, this.epicsId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id='" + this.id + '\'' +
                ", name='" + this.name + '\'' +
                ", description='" + this.description +'\'' +
                ", status='" + this.status + '\'' +
                ", epicsId='" + this.epicsId + '}';
    }
}
