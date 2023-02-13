package ru.icecubenext.kanban.managers.impl.http;

import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.regex.Pattern;

public enum Endpoint {
    GET_TASK,
    GET_TASKS,
    GET_EPIC,
    GET_EPICS,
    GET_SUBTASK,
    GET_SUBTASKS,
    DELETE_TASK,
    DELETE_TASKS,
    DELETE_EPIC,
    DELETE_EPICS,
    DELETE_SUBTASK,
    DELETE_SUBTASKS,
    POST_TASK,
    POST_EPIC,
    POST_SUBTASK,
    GET_HISTORY,
    GET_PRIORITIZED_TASKS,
    UNKNOWN;

    public static Endpoint getEndpoint(HttpExchange exchange) {
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

}

