package ru.icecubenext.kanban.managers;

import com.google.gson.Gson;
import ru.icecubenext.kanban.managers.impl.file.FileBackedTaskManager;
import ru.icecubenext.kanban.managers.impl.memory.InMemoryHistoryManager;
import ru.icecubenext.kanban.managers.impl.memory.InMemoryTaskManager;

public class Manager {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static TaskManager getFileBackedManager() {
        return new FileBackedTaskManager();
    }
    public static TaskManager getInMemoryManager() {
        return new InMemoryTaskManager();
    }
    public static Gson getGson() {
        return new Gson();
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
