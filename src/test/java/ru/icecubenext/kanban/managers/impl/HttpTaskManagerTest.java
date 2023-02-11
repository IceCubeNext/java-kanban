package ru.icecubenext.kanban.managers.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.icecubenext.kanban.managers.impl.http.HttpTaskManager;
import ru.icecubenext.kanban.managers.impl.http.KVServer;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;
    @BeforeEach
    public void serverStart() {
        kvServer.start();
    }

    @AfterEach
    public void serverStop() {
        kvServer.stop();
    }
}
