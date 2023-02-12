package ru.icecubenext.kanban.managers.impl.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j;
import ru.icecubenext.kanban.managers.Manager;
import ru.icecubenext.kanban.managers.impl.file.FileBackedTaskManager;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Log4j
public class HttpTaskManager extends FileBackedTaskManager {
    private String serverURL = "http://localhost:8078";
    private final Gson gson = Manager.getGson();
    private final KVTaskClient client = new KVTaskClient(serverURL);

    public HttpTaskManager() throws IOException, InterruptedException {
    }

    public HttpTaskManager(String serverURL) throws IOException, InterruptedException {
        this.serverURL = serverURL;
    }

    public static HttpTaskManager loadFromServer(String serverURL) throws IOException, InterruptedException {
        final HttpTaskManager taskManager = new HttpTaskManager(serverURL);
        log.debug("Начинаю первоначальную загрузку данных с сервера " + serverURL + ".");
        KVTaskClient client = new KVTaskClient(serverURL);
        Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy HH:mm").create();
        String tasksJson = client.load("Tasks");
        String epicsJson = client.load("Epics");
        String subtasksJson = client.load("Subtasks");
        String historyJson = client.load("History");

        int maxId = 0;
        HashMap<Integer, Task> historyMap = new HashMap<>();
        List<Integer> historyId = gson.fromJson(historyJson, new TypeToken<List<Integer>>(){}.getType());
        if (!historyJson.isEmpty()) {
            for (Integer id : historyId) {
                historyMap.put(id, null);
            }
        }

        List<Task> tasks = gson.fromJson(tasksJson, new TypeToken<List<Task>>(){}.getType());
        if (!tasksJson.isEmpty()) {
            for (Task task : tasks){
                taskManager.addTask(task);
                maxId = Math.max(task.getId(), maxId);
                if (historyMap.containsKey(task.getId())) {
                    historyMap.put(task.getId(), task);
                }
            }
        }
        if(!epicsJson.isEmpty()) {
            List<Epic> epics = gson.fromJson(epicsJson, new TypeToken<List<Epic>>(){}.getType());
            for (Epic epic: epics) {
                epic.setSubtasks(new ArrayList<>());
                taskManager.addEpic(epic);
                maxId = Math.max(epic.getId(), maxId);
                if (historyMap.containsKey(epic.getId())) {
                    historyMap.put(epic.getId(), epic);
                }
            }

            if (!subtasksJson.isEmpty()) {
                List<Subtask> subtasks = gson.fromJson(subtasksJson, new TypeToken<List<Subtask>>(){}.getType());
                for (Subtask subtask : subtasks) {
                    taskManager.addSubtask(subtask);
                    maxId = Math.max(subtask.getId(), maxId);
                    if (historyMap.containsKey(subtask.getId())) {
                        historyMap.put(subtask.getId(), subtask);
                    }
                }
            }
        }
        if (!historyJson.isEmpty()) {
            for (Integer id : historyId) {
                if (historyMap.get(id) != null) {
                    taskManager.addToHistory(historyMap.get(id));
                }
            }
        }
        taskManager.setCurrentId(maxId + 1);
        log.debug("Загрузка завершена. Tasks=" + taskManager.getTasks().size()
                + ", Epics=" + taskManager.getEpics().size()
                + ", Subtasks=" + taskManager.getSubtasks().size()
                + ", History=" + taskManager.getHistory().size());
        return taskManager;
    }

    @Override
    protected void save() {
        client.put("Tasks", gson.toJson(getTasks()));
        client.put("Epics", gson.toJson(getEpics()));
        client.put("Subtasks", gson.toJson(getSubtasks()));
        if (getHistory().size() > 0) {
            List<Integer> historyId = new ArrayList<>();
            for (Task task: getHistory()) {
                historyId.add(task.getId());
            }
            client.put("History", gson.toJson(historyId));
        }
    }
}
