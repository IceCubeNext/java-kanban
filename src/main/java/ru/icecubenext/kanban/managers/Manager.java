package ru.icecubenext.kanban.managers;

import ru.icecubenext.kanban.managers.impl.InMemoryHistoryManager;
import ru.icecubenext.kanban.managers.impl.InMemoryTaskManager;

public class Manager {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
