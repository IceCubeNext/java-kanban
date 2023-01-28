package ru.icecubenext.kanban.model;

import ru.icecubenext.kanban.model.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicsId;
    private final TaskType taskType = TaskType.SUBTASK;

    public Subtask(int id, int epicsId, String name, String description, LocalDateTime startTime, int duration) {
        super(id, name, description, startTime, duration);
        this.epicsId = epicsId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(int id, int epicsId, String name, String description) {
        super(id, name, description);
        this.epicsId = epicsId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(int epicsId, String name, String description) {
        super(name, description);
        this.epicsId = epicsId;
        this.type = TaskType.SUBTASK;
    }

    public int getEpicsId() {
        return epicsId;
    }

    public void setEpicsId(int epicsId) {
        this.epicsId = epicsId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Subtask subtask = (Subtask) obj;
        return this.getId() == subtask.getId()
                && Objects.equals(this.getName(), subtask.getName())
                && Objects.equals(this.getDescription(), subtask.getDescription())
                && Objects.equals(this.getStatus(), subtask.getStatus())
                && this.epicsId == subtask.epicsId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getName(), this.getDescription(),
                this.getStatus(), this.getEpicsId());
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() +'\'' +
                ", status='" + this.getStatus() + '\'' +
                ", epicsId='" + this.getEpicsId() + "'}";
    }
}
