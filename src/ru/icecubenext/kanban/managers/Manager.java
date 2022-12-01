package ru.icecubenext.kanban.managers;

public class Manager {
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
