package ru.icecubenext.kanban.managers.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.icecubenext.kanban.managers.Manager;
import ru.icecubenext.kanban.managers.impl.http.HttpTaskServer;
import ru.icecubenext.kanban.managers.impl.http.KVServer;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;
import ru.icecubenext.kanban.model.enums.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    KVServer kvServer;
    HttpTaskServer managerServer;
    String serverUri = "http://localhost:8080";
    HttpClient client = HttpClient.newHttpClient();
    Gson gson = Manager.getGson();

    @BeforeEach
    public void serverStart() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        managerServer = new HttpTaskServer();
        managerServer.start();
    }

    @AfterEach
    public void ServersStop() {
        kvServer.stop();
        managerServer.stop();
    }

    @Test
    public void addTask() throws IOException, InterruptedException {
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        Task task = new Task(0, "Задача 1", "Описание з. 1", startTime1,0);
        String json = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        task.setId(id);
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/task/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task taskFromServer = gson.fromJson(response.body(), Task.class);
        assertEquals(task, taskFromServer, "задачи не совпадают");
    }

    @Test
    public void updateTask() throws IOException, InterruptedException {
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        Task task = new Task(0, "Задача 1", "Описание з. 1", startTime1,0);
        String json = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        task.setId(id);
        task.setName("Задача 2");
        task.setName("Описание 2");
        task.setStatus(Status.DONE);
        json = gson.toJson(task);
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/task/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task taskFromServer = gson.fromJson(response.body(), Task.class);
        assertEquals(task, taskFromServer, "задачи не совпадают");
    }

    @Test
    public void addEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Эпик 1", "Описание э. 1", null,0);
        String json = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        epic.setId(id);
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/epic/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic epicFromServer = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic, epicFromServer, "Эпики не совпадают");
    }

    @Test
    public void updateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Эпик 1", "Описание э. 1", null,0);
        String json = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        epic.setId(id);
        epic.setName("Эпик 2");
        epic.setName("Описание 2");
        json = gson.toJson(epic);
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/epic/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic epicFromServer = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic, epicFromServer, "Эпики не совпадают");
    }

    @Test
    public void addSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Эпик 1", "Описание э. 1", null,0);
        String json = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        Subtask subtask = new Subtask(0, id, "Подзадача", "Описание", null, 0);
        json = gson.toJson(subtask);
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/subtask/?id=0"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int subtaskId = Integer.parseInt(response.body());
        subtask.setId(subtaskId);
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/subtask/?id=" + subtaskId))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask subtaskFromServer = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask, subtaskFromServer, "Подзадачи не совпадают");
    }

    @Test
    public void updateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Эпик 1", "Описание э. 1", null,0);
        String json = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        Subtask subtask = new Subtask(0, id, "Подзадача", "Описание", null, 0);
        json = gson.toJson(subtask);
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/subtask/?id=0"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int subtaskId = Integer.parseInt(response.body());
        subtask.setId(subtaskId);
        subtask.setName("Подзадача 2");
        epic.setName("Описание 2");
        json = gson.toJson(subtask);
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/subtask/?id=" + subtaskId))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/subtask/?id=" + subtaskId))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask subtaskFromServer = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask, subtaskFromServer, "Подзадачи не совпадают");
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        Task task = new Task(0, "Задача 1", "Описание з. 1", null,0);
        String json = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        task.setId(id);
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/task/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task taskFromServer = gson.fromJson(response.body(), Task.class);
        assertEquals(task, taskFromServer, "задачи не совпадают");
        httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(serverUri + "/tasks/task/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/task/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "задача не удалена");
    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Эпик 1", "Описание э. 1", null,0);
        String json = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        epic.setId(id);
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/epic/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic epicFromServer = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic, epicFromServer, "Эпики не совпадают");
        httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(serverUri + "/tasks/epic/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/epic/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Эпик не удален");
    }

    @Test
    public void deleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Эпик 1", "Описание э. 1", null,0);
        String json = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        Subtask subtask = new Subtask(0, id, "Подзадача", "Описание", null, 0);
        json = gson.toJson(subtask);
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/subtask/?id=0"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int subtaskId = Integer.parseInt(response.body());
        subtask.setId(subtaskId);
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/subtask/?id=" + subtaskId))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask subtaskFromServer = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask, subtaskFromServer, "Подзадачи не совпадают");
        httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(serverUri + "/tasks/subtask/?id=" + subtaskId))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/subtask/?id=" + subtaskId))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "подзадача не удалена");
    }

    @Test
    public void deleteTasks() throws IOException, InterruptedException {
        Task task = new Task(0, "Задача 1", "Описание з. 1", null,0);
        String json = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        task.setId(id);
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/task/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task taskFromServer = gson.fromJson(response.body(), Task.class);
        assertEquals(task, taskFromServer, "задачи не совпадают");
        httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(serverUri + "/tasks/task/"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/task"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "задачи не удалена");
    }

    @Test
    public void deleteEpics() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Эпик 1", "Описание э. 1", null,0);
        String json = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        epic.setId(id);
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/epic/?id=" + id))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic epicFromServer = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic, epicFromServer, "Эпики не совпадают");
        httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(serverUri + "/tasks/epic/"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/epic/"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Эпики не удален");
    }

    @Test
    public void deleteSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Эпик 1", "Описание э. 1", null,0);
        String json = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        Subtask subtask = new Subtask(0, id, "Подзадача", "Описание", null, 0);
        json = gson.toJson(subtask);
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/subtask/?id=0"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int subtaskId = Integer.parseInt(response.body());
        subtask.setId(subtaskId);
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/subtask/?id=" + subtaskId))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask subtaskFromServer = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask, subtaskFromServer, "Подзадачи не совпадают");
        httpRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(serverUri + "/tasks/subtask/"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/subtask/"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "подзадачи не удалена");
    }

    @Test
    public void getEmptyPrioritizedTasks() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void getPrioritizedTasks() throws IOException, InterruptedException {
        LocalDateTime startTime = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 7, 30);
        Task task = new Task(0, "Задача 1", "Описание з. 1", startTime,0);
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", startTime1,0);
        String jsonTask1 = gson.toJson(task);
        String jsonTask2 = gson.toJson(task2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        task.setId(Integer.parseInt(response.body()));
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=0"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        task2.setId(Integer.parseInt(response.body()));
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(List.of(task2, task), tasks);
    }

    @Test
    public void getEmptyHistory() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/history/"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        Task task = new Task(0, "Задача 1", "Описание з. 1", null,0);
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", null,0);
        String jsonTask1 = gson.toJson(task);
        String jsonTask2 = gson.toJson(task2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        task.setId(Integer.parseInt(response.body()));
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=0"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        task2.setId(Integer.parseInt(response.body()));
        for (int i = 1; i < 3; i++) {
            httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(serverUri + "/tasks/task/?id=" + i))
                    .build();
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
        }
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/history/"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(List.of(task, task2), tasks);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void getEmptyTasks() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/task/"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void getTasks() throws IOException, InterruptedException {
        Task task = new Task(0, "Задача 1", "Описание з. 1", null,0);
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", null,0);
        String jsonTask1 = gson.toJson(task);
        String jsonTask2 = gson.toJson(task2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        task.setId(Integer.parseInt(response.body()));
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/task/?id=0"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        task2.setId(Integer.parseInt(response.body()));

        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/task/"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(List.of(task, task2), tasks);
    }

    @Test
    public void getEmptyEpics() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/epic/"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void getEpics() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Эпик 1", "Описание э. 1", null,0);
        Epic epic2 = new Epic(0, "Эпик 2", "Описание э. 2", null,0);
        String epic1json = gson.toJson(epic);
        String epic2json = gson.toJson(epic2);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epic1json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        epic.setId(Integer.parseInt(response.body()));
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(epic2json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        epic2.setId(Integer.parseInt(response.body()));
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/epic/"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> epics = gson.fromJson(response.body(), new TypeToken<List<Epic>>(){}.getType());
        assertEquals(List.of(epic, epic2), epics);
    }

    @Test
    public void getEmptySubtasks() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/subtask/"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void getSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Эпик 1", "Описание э. 1", null,0);
        String json = gson.toJson(epic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/epic/?id=0"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        int id = Integer.parseInt(response.body());
        Subtask subtask = new Subtask(0, id, "Подзадача", "Описание", null, 0);
        Subtask subtask2 = new Subtask(0, id, "Подзадача 2", "Описание 2", null, 0);
        json = gson.toJson(subtask);
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/subtask/?id=0"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        subtask.setId(Integer.parseInt(response.body()));
        json = gson.toJson(subtask2);
        httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Accept", "application/json")
                .uri(URI.create(serverUri + "/tasks/subtask/?id=0"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        subtask2.setId(Integer.parseInt(response.body()));
        httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/subtask/"))
                .build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> subtasks = gson.fromJson(response.body(), new TypeToken<List<Subtask>>(){}.getType());
        assertEquals(List.of(subtask, subtask2), subtasks);
    }

}
