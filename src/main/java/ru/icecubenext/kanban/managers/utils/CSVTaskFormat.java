package ru.icecubenext.kanban.managers.utils;

import lombok.extern.log4j.Log4j;
import ru.icecubenext.kanban.managers.HistoryManager;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;
import ru.icecubenext.kanban.model.enums.Status;
import ru.icecubenext.kanban.model.enums.TaskType;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class CSVTaskFormat {

    public static Task fromString(String str) {
        String[] tokens = str.split(",");
        if (tokens.length < 5 || tokens.length > 6) {
            log.error("Строка не является CSV записью объекта - наследника класса Task.");
            log.error("> " + str);
            return null;
        } else {
            int id = Integer.parseInt(tokens[0]);
            TaskType taskType = TaskType.valueOf(tokens[1]);
            String name = tokens[2];
            Status status = Status.valueOf(tokens[3]);
            String description = tokens[4];
            switch (taskType) {
                case TASK:
                    Task task = new Task(id, name, description);
                    task.setStatus(status);
                    return task;
                case EPIC:
                    Epic epic = new Epic(id, name, description, null);
                    epic.setStatus(status);
                    return epic;
                case SUBTASK:
                    int epicId = Integer.parseInt(tokens[5]);
                    Subtask subtask = new Subtask(id, epicId, name, description);
                    subtask.setStatus(status);
                    return subtask;
                default:
                    log.error("Неизвестный тип задачи! " + tokens[0]);
                    log.error("> " + str);
                    return null;
            }
        }
    }

    public static String toString(Task task) {
        if (task == null) return "";
        String result = task.getId() + ",";
        switch (task.getClass().getSimpleName()) {
            case "Task":
                result += TaskType.TASK + ",";
                break;
            case "Epic":
                result += TaskType.EPIC + ",";
                break;
            case "Subtask":
                result += TaskType.SUBTASK + ",";
                break;
            default:
                return "";
        }
        result += task.getName() + "," + task.getStatus() + "," + task.getDescription();
        if (task.getClass().getSimpleName().equals("Subtask")) {
            result += "," + ((Subtask) task).getEpicsId();
        }
        return result;
    }

    public static String historyToString(HistoryManager manager) {
        List<String> historyId = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            historyId.add(String.valueOf(task.getId()));
        }
        return String.join(",", historyId);
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> historyId = new ArrayList<>();
        for (String id : value.split(",")) {
            historyId.add(Integer.valueOf(id));
        }
        return historyId;
    }

}
