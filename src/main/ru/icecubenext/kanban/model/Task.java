package main.ru.icecubenext.kanban.model;

import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status = Status.NEW;

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description) {
        this.id = 0;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
