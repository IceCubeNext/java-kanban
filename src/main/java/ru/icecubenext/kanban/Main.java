package ru.icecubenext.kanban;

import ru.icecubenext.kanban.managers.Manager;
import ru.icecubenext.kanban.managers.TaskManager;
import ru.icecubenext.kanban.managers.impl.http.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        TaskManager httpTaskManager = Manager.getDefault();
    }
}

