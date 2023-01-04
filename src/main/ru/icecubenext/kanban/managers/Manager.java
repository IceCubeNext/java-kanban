package main.ru.icecubenext.kanban.managers;

import main.ru.icecubenext.kanban.managers.impl.InMemoryHistoryManager;
import main.ru.icecubenext.kanban.managers.impl.InMemoryTaskManager;

public class Manager {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
