package ru.icecubenext.kanban.managers;

import ru.icecubenext.kanban.model.*;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int addTask(Task task);
    int addEpic(Epic epic);
    int addSubtask(Subtask subtask);
    ArrayList<Task> getTasks();
    ArrayList<Epic> getEpics();
    ArrayList<Subtask> getSubtasks();
    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(int id);
    ArrayList<Subtask> getEpicsSubtasks(int epicId);
    boolean updateTask(Task task);
    boolean updateEpic(Epic epic);
    boolean updateSubtask(Subtask subtask);
    boolean deleteTasks();
    boolean deleteEpics();
    boolean deleteSubtasks();
    boolean deleteTask(int id);
    boolean deleteEpic(int id);
    boolean deleteSubtask(int id);
    int getNewId();
}
