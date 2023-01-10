package ru.icecubenext.kanban.managers;

import ru.icecubenext.kanban.managers.exceptions.ManagerSaveException;
import ru.icecubenext.kanban.managers.impl.FileBackedTasksManager;
import ru.icecubenext.kanban.managers.impl.InMemoryHistoryManager;
import ru.icecubenext.kanban.managers.impl.InMemoryTaskManager;

import java.io.File;

public class Manager {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

//    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
//        return new FileBackedTasksManager(file);
//    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
