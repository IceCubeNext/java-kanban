package ru.icecubenext.kanban.model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description, ArrayList<Subtask> subtasks) {
        super(name, description);
        if (subtasks != null) {
            this.subtasks = subtasks;
        }
    }

    public Epic(int id, String name, String description, ArrayList<Subtask> subtasks) {
        super(id, name, description);
        if (subtasks != null) {
            this.subtasks = subtasks;
        }
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        this.subtasks.add(subtask);
    }

    public boolean updateSubtask(Subtask subtask) {
        boolean result = false;
        for (Subtask currentSubtask : this.subtasks) {
            if (subtask.getId() == currentSubtask.getId() && subtask.getEpicsId() == currentSubtask.getEpicsId()) {
                currentSubtask.setName(subtask.getName());
                currentSubtask.setDescription(subtask.getDescription());
                currentSubtask.setStatus(subtask.getStatus());
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean deleteSubtask(Subtask subtask) {
        if (this.subtasks.contains(subtask)) {
            this.subtasks.remove(subtask);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteSubtask(int id) {
        for (int i = 0; i < subtasks.size(); i++) {
            Subtask subtask = subtasks.get(i);
            if (subtask.getId() == id) {
                subtasks.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Status getStatus() {
        if (this.subtasks.size() == 0) {
            return Status.NEW;
        }
        int doneCount = 0;
        int newCount = 0;

        for (Subtask subtask : this.subtasks) {
            if (subtask.getStatus() == Status.DONE) {
                doneCount++;
            } else if (subtask.getStatus() == Status.NEW) {
                newCount++;
            }
        }
        if (doneCount == this.subtasks.size()) {
            return Status.DONE;
        } else if (newCount == this.subtasks.size()) {
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
            result += ", subtasks.size='" + this.getSubtasks().size() + '\'';
        } else {
            result += ", subtasks.size=null";
        }
        return result + '}';
    }
}
