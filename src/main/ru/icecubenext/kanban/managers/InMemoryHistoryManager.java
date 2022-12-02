package main.ru.icecubenext.kanban.managers;

import main.ru.icecubenext.kanban.model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class InMemoryHistoryManager implements HistoryManager {
    int MAX_HISTORY_SIZE = 10;
    private final Queue<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > MAX_HISTORY_SIZE) {
            history.poll();
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
