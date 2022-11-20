package ru.icecubenext.kanban;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(int id, String name, String description, ArrayList<Subtask> subtasks) {
        super(id, name, description);
        setSubtasks(subtasks);
    }

    public Epic(String name, String description, ArrayList<Subtask> subtasks) {
        super(name, description);
        setSubtasks(subtasks);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
        updateStatus();
    }

    public void addSubtask(Subtask subtask) {
        if (this.subtasks == null) {
            this.subtasks = new ArrayList<>();
        }
        this.subtasks.add(subtask);
        updateStatus();
    }

    public boolean updateSubtask(Subtask subtask) {
        if (this.subtasks == null) return false;
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
        updateStatus();
        return result;
    }

    public boolean deleteSubtask(Subtask subtask) {
        if (this.subtasks == null) return false;
        if (this.subtasks.contains(subtask)) {
            this.subtasks.remove(subtask);
            updateStatus();
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteSubtask(int id) {
        if (this.subtasks == null) return false;
        for (int i = 0; i < subtasks.size(); i++) {
            Subtask subtask = subtasks.get(i);
            if (subtask.getId() == id) {
                subtasks.remove(i);
                updateStatus();
                return true;
            }
        }
        return false;
    }

    @Override
    public void setStatus(Status status){
        updateStatus();
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

    private void updateStatus() {
        if (this.subtasks == null || this.subtasks.size() == 0) {
            super.setStatus(Status.NEW);
            return;
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
            super.setStatus(Status.DONE);
        } else if (newCount == this.subtasks.size()) {
            super.setStatus(Status.NEW);
        } else {
            super.setStatus(Status.IN_PROGRESS);
        }
    }
}
