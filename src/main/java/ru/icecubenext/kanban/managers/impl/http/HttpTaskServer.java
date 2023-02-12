package ru.icecubenext.kanban.managers.impl.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.log4j.Log4j;
import ru.icecubenext.kanban.managers.Manager;
import ru.icecubenext.kanban.managers.TaskManager;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Log4j
public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;
    private final HttpServer httpServer;
    private final Gson gson = Manager.getGson();

    public HttpTaskServer() throws IOException {
        taskManager = Manager.getFileBackedManager();
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.httpServer.createContext("/tasks", this::handler);
    }

    public void start() {
        log.debug("Открываем сервер на порту " + PORT);
        this.httpServer.start();
    }

    public void stop() {
        log.debug("Закрываем сервер на порту " + PORT);
        this.httpServer.stop(1);
    }

    private void handler(HttpExchange exchange) throws IOException {
        log.debug(exchange.getRequestMethod() + ": " +  exchange.getRequestURI());
        String requestedMethod = exchange.getRequestMethod();
        switch (requestedMethod) {
            case "GET":
                getHandler(exchange);
                break;
            case "POST":
                postHandler(exchange);
                break;
            case "DELETE":
                deleteHandler(exchange);
                break;
            default:
                writeResponse(exchange, "Unsupported method: " + requestedMethod, 400);
        }
    }

    private void getHandler(HttpExchange exchange) throws IOException {
        final String query = exchange.getRequestURI().getQuery();
        String response;
        Endpoint endpoint = getEndpoint(exchange);
        switch (endpoint) {
            case GET_TASK:
                Task task = taskManager.getTask(Integer.parseInt(query.substring(3)));
                if (task == null) {
                    writeResponse(exchange, "Task not found", 404);
                } else {
                    response = gson.toJson(task);
                    writeResponse(exchange, response, 200);
                }
                break;
            case GET_TASKS:
                if (taskManager.getTasks().size() > 0) {
                    response = gson.toJson(taskManager.getTasks());
                    writeResponse(exchange, response, 200);
                } else {
                    writeResponse(exchange, "Tasks not found", 404);
                }
                break;
            case GET_EPIC:
                Epic epic = taskManager.getEpic(Integer.parseInt(query.substring(3)));
                if (epic == null) {
                    writeResponse(exchange, "Epic not found", 404);
                } else {
                    response = gson.toJson(epic);
                    writeResponse(exchange, response, 200);
                }
                break;
            case GET_EPICS:
                if (taskManager.getEpics().size() > 0) {
                    response = gson.toJson(taskManager.getEpics());
                    writeResponse(exchange, response, 200);
                } else {
                    writeResponse(exchange, "Epics not found", 404);
                }
                break;
            case GET_SUBTASK:
                Subtask subtask = taskManager.getSubtask(Integer.parseInt(query.substring(3)));
                if (subtask == null) {
                    writeResponse(exchange, "Subtask not found", 404);
                } else {
                    response = gson.toJson(subtask);
                    writeResponse(exchange, response, 200);
                }
                break;
            case GET_SUBTASKS:
                if (taskManager.getSubtasks().size() > 0) {
                    response = gson.toJson(taskManager.getSubtasks());
                    writeResponse(exchange, response, 200);
                } else {
                    writeResponse(exchange, "Subtasks not found", 404);
                }
                break;
            case GET_PRIORITIZED_TASKS:
                if (taskManager.getPrioritizedTasks().size() > 0) {
                    response = gson.toJson(taskManager.getPrioritizedTasks());
                    writeResponse(exchange, response, 200);
                } else {
                    writeResponse(exchange, "Tasks not found", 404);
                }
            case GET_HISTORY:
                if (taskManager.getHistory().size() > 0) {
                    response = gson.toJson(taskManager.getHistory());
                    writeResponse(exchange, response, 200);
                } else {
                    writeResponse(exchange, "History not found", 404);
                }
                break;
            default:
                System.out.println("Получен неверный запрос: " + exchange.getRequestURI().toString());
                writeResponse(exchange, "Unsupported endpoint", 404);
        }
    }

    private void postHandler(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        String json = readHttpBody(exchange);
        if (json.isEmpty()) {
            log.debug("POST: Body is empty.");
            writeResponse(exchange, "Body is empty", 400);
            return;
        }
        switch (endpoint) {
            case POST_TASK:
                final Task task = gson.fromJson(json, Task.class);
                if (task.getId() == 0) {
                    int id = taskManager.addTask(task);
                    if (id > 0) {
                        writeResponse(exchange, id + "", 200);
                    } else {
                        writeResponse(exchange, id + "", 400);
                    }
                } else {
                    if (taskManager.updateTask(task)) {
                        writeResponse(exchange, "true", 200);
                    } else {
                        writeResponse(exchange, "false", 400);
                    }
                }
                break;
            case POST_EPIC:
                final Epic epic = gson.fromJson(json, Epic.class);
                if (epic.getId() == 0) {
                    int id = taskManager.addEpic(epic);
                    if (id > 0) {
                        writeResponse(exchange, id + "", 200);
                    } else {
                        writeResponse(exchange, id + "", 400);
                    }
                } else {
                    if (taskManager.updateEpic(epic)) {
                        writeResponse(exchange, "true", 200);
                    } else {
                        writeResponse(exchange, "false", 400);
                    }
                }
                break;
            case POST_SUBTASK:
                final Subtask subtask = gson.fromJson(json, Subtask.class);
                if (subtask.getId() == 0) {
                    int id = taskManager.addSubtask(subtask);
                    if (id > 0) {
                        writeResponse(exchange, id + "", 200);
                    } else {
                        writeResponse(exchange, id + "", 400);
                    }
                } else {
                    if (taskManager.updateSubtask(subtask)) {
                        writeResponse(exchange, "true", 200);
                    } else {
                        writeResponse(exchange, "false", 400);
                    }
                }
                break;
            default:
                log.debug("Неизвестный эндпоинт: " + endpoint);
        }
    }

    private void deleteHandler(HttpExchange exchange) throws IOException {
        final String query = exchange.getRequestURI().getQuery();
        Endpoint endpoint = getEndpoint(exchange);
        int id;
        switch (endpoint) {
            case DELETE_TASK:
                id = Integer.parseInt(query.substring(3));
                if (taskManager.deleteTask(id)) {
                    writeResponse(exchange, "true", 200);
                } else {
                    writeResponse(exchange, "false", 400);
                }
                break;
            case DELETE_TASKS:
                if (taskManager.deleteTasks()) {
                    writeResponse(exchange, "true", 200);
                } else {
                    writeResponse(exchange, "false", 400);
                }
                break;
            case DELETE_EPIC:
                id = Integer.parseInt(query.substring(3));
                if (taskManager.deleteEpic(id)) {
                    writeResponse(exchange, "true", 200);
                } else {
                    writeResponse(exchange, "false", 400);
                }
                break;
            case DELETE_EPICS:
                if (taskManager.deleteEpics()) {
                    writeResponse(exchange, "true", 200);
                } else {
                    writeResponse(exchange, "false", 400);
                }
                break;
            case DELETE_SUBTASK:
                id = Integer.parseInt(query.substring(3));
                if (taskManager.deleteSubtask(id)) {
                    writeResponse(exchange, "true", 200);
                } else {
                    writeResponse(exchange, "false", 400);
                }
                break;
            case DELETE_SUBTASKS:
                if (taskManager.deleteSubtasks()) {
                    writeResponse(exchange, "true", 200);
                } else {
                    writeResponse(exchange, "false", 400);
                }
                break;
            default:
                System.out.println("Получен неверный запрос: " + exchange.getRequestURI().toString());
                writeResponse(exchange, "Unsupported endpoint", 404);
        }
    }

    private String readHttpBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        return new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
    }

    private Endpoint getEndpoint(HttpExchange exchange) {
        Pattern pattern = Pattern.compile("id=[\\d]+");
        URI uri = exchange.getRequestURI();
        String requestMethod = exchange.getRequestMethod();
        String query = uri.getQuery();
        String[] pathParts = uri.getPath().split("/");
        if (pathParts.length < 2 || !pathParts[1].equals("tasks") || pathParts.length > 3) {
            return Endpoint.UNKNOWN;
        }
        switch (requestMethod) {
            case "GET":
                if (pathParts.length == 2) return Endpoint.GET_PRIORITIZED_TASKS;
                switch (pathParts[2]) {
                    case "task":
                        if (query==null) return Endpoint.GET_TASKS;
                        if (pattern.matcher(query).matches()) return Endpoint.GET_TASK;
                        break;
                    case "epic":
                        if (query==null) return Endpoint.GET_EPICS;
                        if (pattern.matcher(query).matches()) return Endpoint.GET_EPIC;
                        break;
                    case "subtask":
                        if (query==null) return Endpoint.GET_SUBTASKS;
                        if (pattern.matcher(query).matches()) return Endpoint.GET_SUBTASK;
                    case "history":
                        if (query==null) return Endpoint.GET_HISTORY;
                    default:
                        return Endpoint.UNKNOWN;
                }
                break;
            case "POST":
                switch (pathParts[2]) {
                    case "task":
                        if (pattern.matcher(query).matches()) return Endpoint.POST_TASK;
                        break;
                    case "epic":
                        if (pattern.matcher(query).matches()) return Endpoint.POST_EPIC;
                        break;
                    case "subtask":
                        if (pattern.matcher(query).matches()) return Endpoint.POST_SUBTASK;
                    default:
                        return Endpoint.UNKNOWN;
                }
                break;
            case "DELETE":
                switch (pathParts[2]) {
                    case "task":
                        if (query==null) return Endpoint.DELETE_TASKS;
                        if (pattern.matcher(query).matches()) return Endpoint.DELETE_TASK;
                        break;
                    case "epic":
                        if (query==null) return Endpoint.DELETE_EPICS;
                        if (pattern.matcher(query).matches())  return Endpoint.DELETE_EPIC;
                        break;
                    case "subtask":
                        if (query==null) return Endpoint.DELETE_SUBTASKS;
                        if (pattern.matcher(query).matches()) return Endpoint.DELETE_SUBTASK;
                    default:
                        return Endpoint.UNKNOWN;
                }
                break;
            default:
                return Endpoint.UNKNOWN;
        }
        return Endpoint.UNKNOWN;
    }


    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}
