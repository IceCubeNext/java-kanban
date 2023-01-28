package ru.icecubenext.kanban.model;

import ru.icecubenext.kanban.model.enums.Status;
import ru.icecubenext.kanban.model.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status = Status.NEW;
    private LocalDateTime startTime;
    private int duration;
    protected TaskType type = TaskType.TASK;

    public Task(int id, String name, String description, LocalDateTime startTime, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime.isBefore(LocalDateTime.now()) ? LocalDateTime.now() : startTime;
        this.duration = Math.max(duration, 0);
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.now();
        this.duration = 15;
    }

    public Task(String name, String description) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.now();
        this.duration = 15;
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

    public TaskType getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int minutes) {
       this.duration = Math.max(minutes, 0);
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(Duration.ofMinutes(duration));
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
