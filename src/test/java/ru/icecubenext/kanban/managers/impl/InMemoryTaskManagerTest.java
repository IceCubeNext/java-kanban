package ru.icecubenext.kanban.managers.impl;

import org.junit.jupiter.api.BeforeEach;
import ru.icecubenext.kanban.managers.impl.memory.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setUp() {
        setTaskManager(new InMemoryTaskManager());
    }

}
