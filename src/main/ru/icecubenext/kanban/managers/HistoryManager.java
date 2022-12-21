package main.ru.icecubenext.kanban.managers;

import main.ru.icecubenext.kanban.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
