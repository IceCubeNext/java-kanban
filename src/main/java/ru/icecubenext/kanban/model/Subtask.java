package ru.icecubenext.kanban.model;

import java.util.Objects;

public class Subtask extends Task {
    private int epicsId;

    public Subtask(int id, int epicsId, String name, String description) {
        super(id, name, description);
        this.epicsId = epicsId;
    }

    public Subtask(int epicsId, String name, String description) {
        super(name, description);
        this.epicsId = epicsId;
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
