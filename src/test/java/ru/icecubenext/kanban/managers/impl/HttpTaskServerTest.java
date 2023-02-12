package ru.icecubenext.kanban.managers.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.icecubenext.kanban.managers.TaskManager;
import ru.icecubenext.kanban.managers.impl.http.HttpTaskManager;
import ru.icecubenext.kanban.managers.impl.http.KVServer;
import ru.icecubenext.kanban.managers.impl.http.KVTaskClient;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    KVServer kvServer;
    String serverUri = "http://localhost:8080";
    HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    public void serverStart() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterEach
    public void serverStop() {
        kvServer.stop();
    }

    @Test
    public void getEmptyPrioritizedTasks() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/?API_TOKEN=DEBUG"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void getPrioritizedTasks() throws IOException, InterruptedException {
        TaskManager taskManager = new HttpTaskManager();
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime3 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        Task task1 = new Task(0, "Задача 1", "Описание з. 1", startTime1,0);
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", startTime2,0);
        Epic epic = new Epic("Эпик1", "Описание э. 1");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask(0, epicId, "Подзадача", "Описание", startTime3, 0);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addSubtask(subtask);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUri + "/tasks/?API_TOKEN=DEBUG"))
                .build();
        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
}
