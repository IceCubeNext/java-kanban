package ru.icecubenext.kanban.managers.impl;

import lombok.extern.log4j.Log4j;
import ru.icecubenext.kanban.managers.TaskManager;
import ru.icecubenext.kanban.managers.exceptions.ManagerSaveException;
import ru.icecubenext.kanban.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

@Log4j
public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final Path managerDataFile;

    public FileBackedTasksManager(Path managerDataFile) {
        this.managerDataFile = managerDataFile;
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
        try (Writer fileWriter = new FileWriter(managerDataFile.toFile())) {
            throw new ManagerSaveException();
        } catch (ManagerSaveException e) {
            log.error("Ошибка сохранения! " + e);
        } catch (IOException e) {
            log.error("Ошибка сохранения! " + e);
        }
    }
}
