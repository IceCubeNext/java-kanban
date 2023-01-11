package ru.icecubenext.kanban.managers.impl;

import lombok.extern.log4j.Log4j;
import ru.icecubenext.kanban.managers.exceptions.ManagerSaveException;
import ru.icecubenext.kanban.managers.utils.CSVTaskFormat;
import ru.icecubenext.kanban.model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Log4j
public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path managerDataFile;
    public FileBackedTasksManager() {
        this.managerDataFile = Paths.get(System.getProperty("user.home"), "kanban.csv");
    }

    public FileBackedTasksManager(Path path) {
        String fileName = path.getFileName().toString();
        int index = fileName.lastIndexOf('.');
        if (index > 0 && fileName.substring(index + 1).equals("csv")) {
            this.managerDataFile = path;
        } else {
            throw new RuntimeException("Поддерживается работа только с файлами csv!");
        }
    }

    public static FileBackedTasksManager loadFromFile(Path path) {
        final FileBackedTasksManager taskManager = new FileBackedTasksManager(path);
        try {
            int maxId = 0;
            String content = Files.readString(taskManager.managerDataFile);
            String[] tasks = content.split("\n");
            if (tasks.length < 3) {
                log.error("Нарушена целостность файла. Невозможно загрузить данные.");
                throw new RuntimeException("Нарушена целостность файла. Невозможно загрузить данные.");
            }

            HashMap<Integer, Task> historyMap = new HashMap<>();
            for (String s : tasks[tasks.length - 1].split(",")) {
                historyMap.put(Integer.valueOf(s), null);
            }

            for (int i = 1; i < tasks.length - 2; i++){
                Task task = CSVTaskFormat.fromString(tasks[i]);
                if (task == null) continue;
                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
                if (historyMap.containsKey(task.getId())) {
                    historyMap.put(task.getId(), task);
                }
                switch (task.getClass().getSimpleName()) {
                    case "Task":
                        taskManager.addTask(task);
                        break;
                    case "Epic":
                        Epic epic = (Epic) task;
                        taskManager.addEpic(epic);
                        break;
                    case "Subtask":
                        Subtask subtask = (Subtask) task;
                        taskManager.addSubtask(subtask);
                        break;
                }
            }

            for (Integer id : CSVTaskFormat.historyFromString(tasks[tasks.length - 1])) {
                if (historyMap.get(id) != null) {
                    taskManager.addToHistory(historyMap.get(id));
                }
            }

            taskManager.setCurrentId(maxId + 1);
        } catch (IOException e) {
            log.error("Ошибка при чтении файла! " + e);
        }
        return taskManager;
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

    private void save() {
        try (Writer fileWriter = new FileWriter(managerDataFile.toFile(), StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                fileWriter.write(CSVTaskFormat.toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                fileWriter.write(CSVTaskFormat.toString(epic) + "\n");
                for (Subtask subtask : getEpicsSubtasks(epic.getId())){
                    fileWriter.write(CSVTaskFormat.toString(subtask) + "\n");
                }
            }
            fileWriter.write("\n");
            fileWriter.write(CSVTaskFormat.historyToString(historyManager));
        } catch (IOException e) {
            log.error("Ошибка сохранения! " + e);
            throw new ManagerSaveException("Ошибка сохранения! " + e);
        }
    }
}
