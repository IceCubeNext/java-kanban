package ru.icecubenext.kanban.managers.impl;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setUp() {
        setTaskManager(new InMemoryTaskManager());
    }

}
