package ru.icecubenext.kanban.managers.utils;

import lombok.extern.log4j.Log4j;
import ru.icecubenext.kanban.managers.HistoryManager;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;
import ru.icecubenext.kanban.model.enums.Status;
import ru.icecubenext.kanban.model.enums.TaskType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j
public class CSVTaskFormat {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public static Task fromString(String str) {
        String[] tokens = str.split(",");
        if (tokens.length < 7 || tokens.length > 8) {
            log.error("Строка не является CSV записью объекта - наследника класса Task.");
            log.error("> " + str);
            return null;
        } else {
            int id = Integer.parseInt(tokens[0]);
            TaskType taskType = TaskType.valueOf(tokens[1]);
            String name = tokens[2];
            Status status = Status.valueOf(tokens[3]);
            String description = tokens[4];
            LocalDateTime startTime;
            if (!tokens[5].equals("null")) {
                startTime = LocalDateTime.parse(tokens[5], formatter);
            } else {
                startTime = null;
            }

            int duration = Integer.parseInt(tokens[6]);
            switch (taskType) {
                case TASK:
                    Task task = new Task(id, name, description, startTime, duration);
                    task.setStatus(status);
                    return task;
                case EPIC:
                    Epic epic = new Epic(id, name, description, startTime, duration, null);
                    epic.setStatus(status);
                    return epic;
                case SUBTASK:
                    int epicId = Integer.parseInt(tokens[7]);
                    Subtask subtask = new Subtask(id, epicId, name, description, startTime, duration);
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
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(String.valueOf(task.getId()));
        switch (task.getType()) {
            case TASK:
                joiner.add(TaskType.TASK.toString());
                break;
            case EPIC:
                joiner.add(TaskType.EPIC.toString());
                break;
            case SUBTASK:
                joiner.add(TaskType.SUBTASK.toString());
                break;
            default:
                return "";
        }
        String startTime = (task.getStartTime() == null) ? "null" : task.getStartTime().format(formatter);
        joiner.add(task.getName())
                .add(task.getStatus().toString())
                .add(task.getDescription())
                .add(startTime)
                .add(String.valueOf(task.getDuration()));
        if (task.getClass().getSimpleName().equals("Subtask")) {
            joiner.add(String.valueOf(((Subtask) task).getEpicsId()));
        }
        return joiner.toString();
    }

    public static String historyToString(HistoryManager manager) {
        List<String> historyId = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            historyId.add(String.valueOf(task.getId()));
        }
        return historyId.size() == 0 ? " " : String.join(",", historyId);
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> historyId = new ArrayList<>();
        Pattern pattern = Pattern.compile("([\\d,])+");
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            for (String id : value.split(",")) {
                historyId.add(Integer.valueOf(id));
            }
        }
        return historyId;
    }

}
