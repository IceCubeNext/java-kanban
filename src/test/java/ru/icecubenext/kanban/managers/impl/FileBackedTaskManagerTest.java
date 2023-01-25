package ru.icecubenext.kanban.managers.impl;

import org.junit.jupiter.api.BeforeEach;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @BeforeEach
    public void setUp() {
        setTaskManager(new FileBackedTaskManager());
    }
}
