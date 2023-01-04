package main.ru.icecubenext.kanban.managers.impl;

import main.ru.icecubenext.kanban.managers.TaskManager;

import java.nio.file.Path;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final Path managerDataFile;

    public FileBackedTasksManager(Path managerDataFile) {
        this.managerDataFile = managerDataFile;
    }

    private void save() {

    }
}
