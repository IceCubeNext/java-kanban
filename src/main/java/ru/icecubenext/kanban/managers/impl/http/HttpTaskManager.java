package ru.icecubenext.kanban.managers.impl.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.icecubenext.kanban.managers.Manager;
import ru.icecubenext.kanban.managers.impl.file.FileBackedTaskManager;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTaskManager {
    URI serverURL;
    Gson gson = Manager.getGson();
    KVTaskClient client;

    public HttpTaskManager() throws IOException, InterruptedException {
        this.serverURL = URI.create("http://localhost:8078");
        this.client = new KVTaskClient("http://localhost:8078");
        loadFromServer();
    }

    public HttpTaskManager(String serverURL) throws IOException, InterruptedException {
        this.serverURL = URI.create(serverURL);
        this.client = new KVTaskClient(serverURL);
        loadFromServer();
    }

    private void loadFromServer() {
        String tasksJson = client.load("Tasks");
        String epicsJson = client.load("Epics");
        String subtasksJson = client.load("Subtasks");
        String historyJson = client.load("History");
        List<Task> tasks = gson.fromJson(tasksJson, new TypeToken<ArrayList<Task>>(){}.getType());
        if (!tasksJson.isEmpty()) {
            for (Task task : tasks){
                addTask(task);
            }
        }
        if(!epicsJson.isEmpty()) {
            List<Epic> epics = gson.fromJson(epicsJson, new TypeToken<ArrayList<Epic>>(){}.getType());
            for (Epic epic: epics) {
                addEpic(epic);
            }
            if (!subtasksJson.isEmpty()) {
                List<Subtask> subtasks = gson.fromJson(subtasksJson, new TypeToken<ArrayList<Subtask>>(){}.getType());
                for (Subtask subtask : subtasks) {
                    addSubtask(subtask);
                }
            }
        }
        if (!historyJson.isEmpty()) {
            List<Task> history = gson.fromJson(historyJson, new TypeToken<ArrayList<Task>>(){}.getType());
            for (Task task : history) {
                addToHistory(task);
            }
        }
    }

    @Override
    protected void save() {
        client.put("Tasks", gson.toJson(getTasks()));
        client.put("Epics", gson.toJson(getEpics()));
        client.put("Subtasks", gson.toJson(getSubtasks()));
        client.put("History", gson.toJson(getHistory()));
    }
}
