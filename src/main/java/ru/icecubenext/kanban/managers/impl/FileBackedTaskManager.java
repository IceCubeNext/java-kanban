package ru.icecubenext.kanban.managers.impl;

import lombok.extern.log4j.Log4j;
import ru.icecubenext.kanban.managers.exceptions.ManagerSaveException;
import ru.icecubenext.kanban.managers.utils.CSVTaskFormat;
import ru.icecubenext.kanban.model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j
public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File managerDataFile;
    public FileBackedTaskManager() {
        this.managerDataFile = Paths.get(System.getProperty("user.home"), "kanban.csv").toFile();
    }

    public FileBackedTaskManager(File file) {
        Pattern pattern = Pattern.compile("(.+(\\.(?i)(csv))$)");
        Matcher matcher = pattern.matcher(file.toString());
        if (matcher.matches()) {
            this.managerDataFile = file;
        } else {
            throw new RuntimeException("Поддерживается работа только с файлами csv!");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            int maxId = 0;
            String content = Files.readString(taskManager.managerDataFile.toPath(), StandardCharsets.UTF_8);
            String[] tasks = content.split("\\r?\\n");
            if (tasks.length < 3) {
                log.error("Нарушена целостность файла. Невозможно загрузить данные.");
                throw new RuntimeException("Нарушена целостность файла. Невозможно загрузить данные.");
            }

            HashMap<Integer, Task> historyMap = new HashMap<>();
            List<Integer> history = CSVTaskFormat.historyFromString(tasks[tasks.length - 1]);
            for (Integer id : history) {
                historyMap.put(id, null);
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
                switch (task.getType()) {
                    case TASK:
                        taskManager.addTask(task);
                        break;
                    case EPIC:
                        Epic epic = (Epic) task;
                        taskManager.addEpic(epic);
                        break;
                    case SUBTASK:
                        Subtask subtask = (Subtask) task;
                        taskManager.addSubtask(subtask);
                        break;
                }
            }
            if (history.size() > 0) {
                for (Integer id : history) {
                    if (historyMap.get(id) != null) {
                        taskManager.addToHistory(historyMap.get(id));
                    }
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
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(managerDataFile, StandardCharsets.UTF_8))) {
            fileWriter.write("id,type,name,status,description,epic");
            fileWriter.newLine();
            for (Task task : getTasks()) {
                fileWriter.write(CSVTaskFormat.toString(task));
                fileWriter.newLine();
            }
            for (Epic epic : getEpics()) {
                fileWriter.write(CSVTaskFormat.toString(epic));
                fileWriter.newLine();
                for (Subtask subtask : getEpicsSubtasks(epic.getId())){
                    fileWriter.write(CSVTaskFormat.toString(subtask));
                    fileWriter.newLine();
                }
            }
            fileWriter.newLine();
            fileWriter.write(CSVTaskFormat.historyToString(historyManager));
        } catch (IOException e) {
            log.error("Ошибка сохранения! " + e);
            throw new ManagerSaveException("Ошибка сохранения! " + e);
        }
    }
}
