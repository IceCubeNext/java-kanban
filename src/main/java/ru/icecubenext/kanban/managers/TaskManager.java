package ru.icecubenext.kanban.managers;

import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;

import java.util.List;

public interface TaskManager {
    int addTask(Task task);
    int addEpic(Epic epic);
    int addSubtask(Subtask subtask);
    List<Task> getTasks();
    List<Epic> getEpics();
    List<Subtask> getSubtasks();
    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(int id);
    List<Subtask> getEpicsSubtasks(int epicId);
    boolean updateTask(Task task);
    boolean updateEpic(Epic epic);
    boolean updateSubtask(Subtask subtask);
    boolean deleteTasks();
    boolean deleteEpics();
    boolean deleteSubtasks();
    boolean deleteTask(int id);
    boolean deleteEpic(int id);
    boolean deleteSubtask(int id);
    List<Task> getHistory();
}
