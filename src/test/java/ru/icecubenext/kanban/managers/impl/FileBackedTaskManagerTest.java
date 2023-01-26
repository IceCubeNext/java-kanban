package ru.icecubenext.kanban.managers.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @BeforeEach
    public void setUp() {
        setTaskManager(new FileBackedTaskManager());
    }

    @Test
    public void saveAndLoad() {
        final File file = Paths.get(System.getProperty("user.home"), "kanban.csv").toFile();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getTasks());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getEpics());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getSubtasks());

        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        Task task3 = new Task("Задача 3", "Описание з. 3");
        int task1Id = fileBackedTaskManager.addTask(task1);
        int task2Id = fileBackedTaskManager.addTask(task2);
        int task3Id = fileBackedTaskManager.addTask(task3);
        fileBackedTaskManager.getTask(task1Id);
        fileBackedTaskManager.getTask(task2Id);
        fileBackedTaskManager.getTask(task3Id);

        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(List.of(task1, task2, task3), fileBackedTaskManager.getTasks());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getEpics());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getSubtasks());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getSubtasks());
        assertEquals(List.of(task1, task2, task3), fileBackedTaskManager.getHistory());

        fileBackedTaskManager.deleteTasks();
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getTasks());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getHistory());

        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        int epic1Id = fileBackedTaskManager.addEpic(epic1);
        int epic2Id = fileBackedTaskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        Subtask subtask3 = new Subtask(epic2Id, "Подзадача 3", "Описание п. 3");
        int subtask1Id = fileBackedTaskManager.addSubtask(subtask1);
        int subtask2Id = fileBackedTaskManager.addSubtask(subtask2);
        int subtask3Id = fileBackedTaskManager.addSubtask(subtask3);
        fileBackedTaskManager.getEpic(epic1Id);
        fileBackedTaskManager.getEpic(epic2Id);
        fileBackedTaskManager.getSubtask(subtask1Id);
        fileBackedTaskManager.getSubtask(subtask2Id);
        fileBackedTaskManager.getSubtask(subtask3Id);
        assertEquals(List.of(epic1, epic2, subtask1, subtask2, subtask3), fileBackedTaskManager.getHistory());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getTasks());
        assertEquals(List.of(epic1, epic2), fileBackedTaskManager.getEpics());
        assertEquals(List.of(subtask1, subtask2, subtask3), fileBackedTaskManager.getSubtasks());

        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(List.of(epic1, epic2, subtask1, subtask2, subtask3), fileBackedTaskManager.getHistory());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getTasks());
        assertEquals(List.of(epic1, epic2), fileBackedTaskManager.getEpics());
        assertEquals(List.of(subtask1, subtask2, subtask3), fileBackedTaskManager.getSubtasks());

        fileBackedTaskManager.deleteSubtasks();
        epic1 = fileBackedTaskManager.getEpic(epic1Id);
        epic2 = fileBackedTaskManager.getEpic(epic1Id);
        assertEquals(List.of(epic1, epic2),fileBackedTaskManager.getHistory());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getTasks());
        assertEquals(List.of(epic1, epic2), fileBackedTaskManager.getEpics());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getSubtasks());

        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(List.of(epic1, epic2),fileBackedTaskManager.getHistory());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getTasks());
        assertEquals(List.of(epic1, epic2), fileBackedTaskManager.getEpics());
        assertEquals(new ArrayList<>(), fileBackedTaskManager.getSubtasks());

    }
}
