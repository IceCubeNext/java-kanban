package ru.icecubenext.kanban.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import ru.icecubenext.kanban.managers.impl.file.FileBackedTaskManager;
import ru.icecubenext.kanban.managers.impl.memory.InMemoryHistoryManager;
import ru.icecubenext.kanban.managers.impl.memory.InMemoryTaskManager;
import ru.icecubenext.kanban.model.Epic;

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
        return new GsonBuilder()
                .setDateFormat("dd.MM.yyyy HH:mm")
                .serializeNulls()
                .create();
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
