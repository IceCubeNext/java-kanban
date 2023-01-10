package ru.icecubenext.kanban.managers.impl;

import lombok.extern.log4j.Log4j;
import ru.icecubenext.kanban.managers.TaskManager;
import ru.icecubenext.kanban.managers.exceptions.ManagerSaveException;
import ru.icecubenext.kanban.model.*;
import ru.icecubenext.kanban.model.enums.Status;
import ru.icecubenext.kanban.model.enums.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Log4j
public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final String managerDataFile;

    public FileBackedTasksManager() {
        this.managerDataFile = System.getProperty("user.home") + "\\kanban.csv";
    }

    public FileBackedTasksManager(String managerDataFile) throws ManagerSaveException {
        Path dir = Paths.get(managerDataFile);
        String fileName = dir.getFileName().toString();
        int index = fileName.lastIndexOf('.');
        if (index > 0 && fileName.substring(index + 1).equals("csv")) {
            this.managerDataFile = managerDataFile;
        } else {
            throw new ManagerSaveException("Поддерживается работа только с файлами csv!");
        }
        loadFromFile();
    }

    @Override
    public Task getTask(int id){
        Task task = super.getTask(id);
        if (task != null) {
            save();
        }
        return task;
    }

    @Override
    public Epic getEpic(int id){
        Epic epic = super.getEpic(id);
        if (epic != null) {
            save();
        }
        return epic;
    }

    @Override
    public Subtask getSubtask(int id){
        Subtask subtask = super.getSubtask(id);
        if (subtask != null) {
            save();
        }
        return subtask;
    }

    @Override
    public int addTask(Task task) {
        int result = super.addTask(task);
        if (result > 0) {
            save();
        }
        return result;
    }

    @Override
    public int addEpic(Epic epic) {
        int result = super.addEpic(epic);
        if (result > 0) {
            save();
        }
        return result;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int result = super.addSubtask(subtask);
        if (result > 0) {
            save();
        }
        return result;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean isUpdate = super.updateTask(task);
        if (isUpdate) {
            save();
        }
        return isUpdate;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean isUpdate = super.updateEpic(epic);
        if (isUpdate) {
            save();
        }
        return isUpdate;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean isUpdate = super.updateSubtask(subtask);
        if (isUpdate) {
            save();
        }
        return isUpdate;
    }

    @Override
    public boolean deleteTasks() {
        boolean isDelete = super.deleteTasks();
        if (isDelete) {
            save();
        }
        return isDelete;
    }

    @Override
    public boolean deleteEpics() {
        boolean isDelete = super.deleteEpics();
        if (isDelete) {
            save();
        }
        return  isDelete;
    }

    @Override
    public boolean deleteSubtasks() {
        boolean isDelete = super.deleteSubtasks();
        if (isDelete) {
            save();
        }
        return isDelete;
    }

    @Override
    public boolean deleteTask(int id) {
        boolean isDelete = super.deleteTask(id);
        if (isDelete) {
            save();
        }
        return isDelete;
    }

    @Override
    public boolean deleteEpic(int id) {
        boolean isDelete = super.deleteEpic(id);
        if (isDelete) {
            save();
        }
        return isDelete;
    }

    @Override
    public  boolean deleteSubtask(int id) {
        boolean isDelete = super.deleteSubtask(id);
        if (isDelete) {
            save();
        }
        return isDelete;
    }

    private void loadFromFile() {
        try {
            int maxId = 0;
            String content = Files.readString(Path.of(managerDataFile));
            String[] tasks = content.split("\n");
            if (tasks.length < 3) {
                log.error("Нарушена целостность файла. Невозможно загрузить данные.");
                return;
            }
            HashMap<Integer, Task> historyMap = new HashMap<>();
            for (String s : tasks[tasks.length - 1].split(",")) {
                historyMap.put(Integer.valueOf(s), null);
            }
            for (int i = 1; i < tasks.length - 2; i++){
                Task task = fromString(tasks[i]);
                if (task == null) continue;
                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
                if (historyMap.containsKey(task.getId())) {
                    historyMap.put(task.getId(), task);
                }
                switch (task.getClass().getSimpleName()) {
                    case "Task":
                        super.addTask(task);
                        break;
                    case "Epic":
                        Epic epic = (Epic) task;
                        super.addEpic(epic);
                        break;
                    case "Subtask":
                        Subtask subtask = (Subtask) task;
                        super.addSubtask(subtask);
                        break;
                }
            }
            for (String s : tasks[tasks.length - 1].split(",")) {
                super.addToHistory(historyMap.get(Integer.valueOf(s)));
            }
            setCurrentId(maxId + 1);
        } catch (IOException e) {
            log.error("Ошибка при чтении файла! " + e);
        }

    }

    private void save() {
        try (Writer fileWriter = new FileWriter(managerDataFile, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                fileWriter.write(toString(task));
            }
            for (Epic epic : getEpics()) {
                fileWriter.write(toString(epic));
                for (Subtask subtask : getEpicsSubtasks(epic.getId())){
                    fileWriter.write(toString(subtask));
                }
            }
            fileWriter.write("\n");
            List<Task> history = getHistory();
            for (int i = 0; i < history.size(); i++) {
                fileWriter.write(Integer.toString(history.get(i).getId()));
                if (i < history.size() - 1) {
                    fileWriter.write(",");
                }
            }
        } catch (IOException e) {
            log.error("Ошибка сохранения! " + e);
        }
    }

    private Task fromString(String str) {
        String[] tokens = str.split(",");
        if (tokens.length < 5 || tokens.length > 6) {
            log.error("Нарушена целостность файла! Невозможно загрузить данные.");
            return null;
        } else {
            int id = Integer.parseInt(tokens[0]);
            TaskType taskType = TaskType.valueOf(tokens[1]);
            String name = tokens[2];
            Status status = Status.valueOf(tokens[3]);
            String description = tokens[4];
            if (taskType.equals(TaskType.TASK)) {
                Task task = new Task(id, name, description);
                task.setStatus(status);
                return task;
            } else if (taskType.equals(TaskType.EPIC)) {
                Epic epic = new Epic(id, name, description, null);
                epic.setStatus(status);
                return epic;
            } else if (taskType.equals(TaskType.SUBTASK)) {
                int epicId = Integer.parseInt(tokens[tokens.length - 1]);
                Subtask subtask = new Subtask(id, epicId, name, description);
                subtask.setStatus(status);
                return subtask;
            } else {
                log.error("Неизвестный тип задачи! Структура файла нарушена. Невозможно загрузить данные.");
                return null;
            }
        }
    }

    private String toString(Task task) {
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
        }
        result += task.getName() + "," + task.getStatus() + "," + task.getDescription();
        if (task.getClass().getSimpleName().equals("Subtask")) {
            result += "," + ((Subtask) task).getEpicsId();
        }
        return result + "\n";
    }
}
