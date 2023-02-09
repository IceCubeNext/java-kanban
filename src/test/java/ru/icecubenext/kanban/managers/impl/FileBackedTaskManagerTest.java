package ru.icecubenext.kanban.managers.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.icecubenext.kanban.managers.TaskManager;
import ru.icecubenext.kanban.managers.impl.file.FileBackedTaskManager;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    public void setUp() {
        setTaskManager(new FileBackedTaskManager());
    }

    @Test
    public void loadWithEmptyFile() {
        TaskManager taskManager = getTaskManager();
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getTasks().size());
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    public void loadFileWithOnlyTasks() {
        final File file = Paths.get(System.getProperty("user.home"), "kanban.csv").toFile();
        TaskManager taskManager = new FileBackedTaskManager(file);
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", null, 0);
        Task task3 = new Task(0, "Задача 2", "Описание з. 2", startTime1, 30);
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        int task3Id = taskManager.addTask(task3);

        taskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(3, taskManager.getTasks().size());
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getHistory().size());
        assertEquals(task1, taskManager.getTask(task1Id));
        assertEquals(task2, taskManager.getTask(task2Id));
        assertEquals(task3, taskManager.getTask(task3Id));
    }

    @Test
    public void loadFileWithEmptyEpics() {
        final File file = Paths.get(System.getProperty("user.home"), "kanban.csv").toFile();
        TaskManager taskManager = new FileBackedTaskManager(file);
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);

        taskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(0, taskManager.getTasks().size());
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getHistory().size());
        assertEquals(epic1, taskManager.getEpic(epic1Id));
        assertEquals(epic2, taskManager.getEpic(epic2Id));
    }

    @Test
    public void loadFileWithSubtasks() {
        final File file = Paths.get(System.getProperty("user.home"), "kanban.csv").toFile();
        TaskManager taskManager = new FileBackedTaskManager(file);
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        Subtask subtask1 = new Subtask(epic1Id,"Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(0, epic1Id, "Подзадача 2", "Описание п. 2", null, 0);
        Subtask subtask3 = new Subtask(0, epic2Id, "Подзадача 3", "Описание п. 3", startTime1, 30);
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);

        taskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(0, taskManager.getTasks().size());
        assertEquals(3, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getHistory().size());
        assertEquals(epic1, taskManager.getEpic(epic1Id));
        assertEquals(epic2, taskManager.getEpic(epic2Id));
        assertEquals(subtask1, taskManager.getSubtask(subtask1Id));
        assertEquals(subtask2, taskManager.getSubtask(subtask2Id));
        assertEquals(subtask3, taskManager.getSubtask(subtask3Id));
    }

    @Test
    public void loadFileAllTypeTasksWithoutHistory() {
        final File file = Paths.get(System.getProperty("user.home"), "kanban.csv").toFile();
        TaskManager taskManager = new FileBackedTaskManager(file);
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 30);
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", null, 0);
        Task task3 = new Task(0, "Задача 2", "Описание з. 2", startTime1, 30);
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        int task3Id = taskManager.addTask(task3);
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic1Id,"Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(0, epic1Id, "Подзадача 2", "Описание п. 2", null, 0);
        Subtask subtask3 = new Subtask(0, epic2Id, "Подзадача 3", "Описание п. 3", startTime2, 30);
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);

        taskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(3, taskManager.getTasks().size());
        assertEquals(3, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getHistory().size());
        assertEquals(epic1, taskManager.getEpic(epic1Id));
        assertEquals(epic2, taskManager.getEpic(epic2Id));
        assertEquals(task1, taskManager.getTask(task1Id));
        assertEquals(task2, taskManager.getTask(task2Id));
        assertEquals(task3, taskManager.getTask(task3Id));
        assertEquals(subtask1, taskManager.getSubtask(subtask1Id));
        assertEquals(subtask2, taskManager.getSubtask(subtask2Id));
        assertEquals(subtask3, taskManager.getSubtask(subtask3Id));
    }

    @Test
    public void loadFileAllTypeTasksWithHistory() {
        final File file = Paths.get(System.getProperty("user.home"), "kanban.csv").toFile();
        TaskManager taskManager = new FileBackedTaskManager(file);
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        Task task = new Task("Задача 1", "Описание з. 1");
        int taskId = taskManager.addTask(task);
        Epic epic = new Epic("Эпик1", "Описание э. 1");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask(0, epicId, "Подзадача 2", "Описание п. 2", startTime1, 0);
        int subtaskId = taskManager.addSubtask(subtask);
        taskManager.getTask(taskId);
        taskManager.getSubtask(subtaskId);
        taskManager.getEpic(epicId);

        taskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(1, taskManager.getTasks().size());
        assertEquals(1, taskManager.getSubtasks().size());
        assertEquals(3, taskManager.getHistory().size());
        assertEquals(List.of(task, subtask, epic), taskManager.getHistory());
    }
}
