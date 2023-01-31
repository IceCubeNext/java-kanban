package ru.icecubenext.kanban.model;

import ru.icecubenext.kanban.model.enums.Status;
import ru.icecubenext.kanban.model.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Subtask> subtasks = new ArrayList<>();

    public Epic(int id, String name, String description, LocalDateTime startTime, int duration){
        super(id, name, description, startTime, duration);
        this.type = TaskType.EPIC;
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public int getDuration() {
        return  getEpicsTimeData().getDuration();
    }

    @Override
    public LocalDateTime getStartTime(){
        return getEpicsTimeData().getStartTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        return getEpicsTimeData().getEndTime();
    }

    @Override
    public Status getStatus() {
        if (subtasks.size() == 0) {
            return Status.NEW;
        }
        int doneCount = 0;
        int newCount = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.DONE) {
                doneCount++;
            } else if (subtask.getStatus() == Status.NEW) {
                newCount++;
            }
        }
        if (doneCount == subtasks.size()) {
            return Status.DONE;
        } else if (newCount == subtasks.size()) {
            return Status.NEW;
        } else {
            return Status.IN_PROGRESS;
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
                && Objects.equals(epic.getStartTime(), this.getStartTime())
                && Objects.equals(epic.getDuration(), this.getDuration())
                && Objects.equals(epic.getSubtasks(), this.getSubtasks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getName(), this.getDescription(),
                this.getStatus(), this.getStartTime(), this.getDuration(), this.getSubtasks());
    }

    @Override
    public String toString() {
        String result =  "Epic{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() +'\'' +
                ", status='" + this.getStatus() + '\'';
        if (this.getStartTime() != null) {
            result += ", startTime='" + this.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +"'";
        } else {
            result += ", startTime='null'";
        }
                result += ", duration='" + this.getDuration() + "' min";
        if (this.getSubtasks() != null) {
            result += ", subtasks.size='" + this.getSubtasks().size() + '\'';
        } else {
            result += ", subtasks.size=null";
        }
        return result + '}';
    }

    private static class EpicsTimeData {
        private Subtask firstSubtask = null;
        private Subtask lastSubtask = null;

        public LocalDateTime getStartTime() {
            if(this.firstSubtask == null) {
                return null;
            } else {
                return firstSubtask.getStartTime();
            }
        }

        public LocalDateTime getEndTime() {
            if(this.firstSubtask == null) {
                return null;
            } else {
                return lastSubtask.getEndTime();
            }
        }

        public int getDuration() {
            if(this.firstSubtask == null) {
                return 0;
            } else if (this.firstSubtask.equals(lastSubtask)){
                return firstSubtask.getDuration();
            } else {
                return (int) Duration.between(firstSubtask.getStartTime(), lastSubtask.getEndTime()).toMinutes();
            }
        }
    }

    private EpicsTimeData getEpicsTimeData() {
        if (subtasks.size() == 0) {
            return new EpicsTimeData();
        }
        Subtask firstSubtask = null;
        Subtask lastSubtask = null;

        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime() != null) {
                if (firstSubtask == null) {
                    firstSubtask = subtask;
                    lastSubtask = subtask;
                } else if (subtask.getStartTime().isBefore(firstSubtask.getStartTime())) {
                    firstSubtask = subtask;
                } else if (subtask.getStartTime().isAfter(lastSubtask.getStartTime())) {
                    lastSubtask = subtask;
                }
            }
        }
        EpicsTimeData timeData = new EpicsTimeData();
        timeData.firstSubtask = firstSubtask;
        timeData.lastSubtask = lastSubtask;
        return timeData;
    }
}
