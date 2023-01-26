package ru.icecubenext.kanban.managers.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.icecubenext.kanban.managers.Manager;
import ru.icecubenext.kanban.managers.TaskManager;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InMemoryHistoryManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void setUp()  {
        taskManager = Manager.getDefault();
    }

    @AfterEach
    public void clearData() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
    }

    @Test
    public void add() {
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Epic epic1 = new Epic("Эпик 1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        int task1Id = taskManager.addTask(task1);
        int subtask1Id = taskManager.addSubtask(subtask1);

        assertEquals(0, taskManager.getHistory().size());
        taskManager.getTask(task1Id);
        assertEquals(1, taskManager.getHistory().size());
        taskManager.getTask(task1Id);
        assertEquals(1, taskManager.getHistory().size());
        taskManager.getEpic(epic1Id);
        assertEquals(2, taskManager.getHistory().size());
        taskManager.getEpic(epic1Id);
        assertEquals(2, taskManager.getHistory().size());
        taskManager.getSubtask(subtask1Id);
        assertEquals(3, taskManager.getHistory().size());
        taskManager.getSubtask(subtask1Id);
        assertEquals(3, taskManager.getHistory().size());
        assertEquals(List.of(task1, epic1, subtask1), taskManager.getHistory());
    }

    @Test
    public void remove() {
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Epic epic1 = new Epic("Эпик 1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        int task1Id = taskManager.addTask(task1);
        int subtask1Id = taskManager.addSubtask(subtask1);
        taskManager.getTask(task1Id);
        taskManager.getEpic(epic1Id);
        taskManager.getSubtask(subtask1Id);
        assertEquals(List.of(task1, epic1, subtask1), taskManager.getHistory());
        taskManager.deleteTask(task1Id);
        assertEquals(List.of(epic1, subtask1), taskManager.getHistory());
        task1Id = taskManager.addTask(task1);
        taskManager.getTask(task1Id);
        assertEquals(List.of(epic1, subtask1, task1), taskManager.getHistory());
        taskManager.deleteSubtask(subtask1Id);
        assertEquals(List.of(epic1, task1), taskManager.getHistory());
        taskManager.deleteTask(task1Id);
        assertEquals(List.of(epic1), taskManager.getHistory());
        taskManager.deleteEpic(epic1Id);
        assertEquals(new ArrayList<>(), taskManager.getHistory());

        List<Task> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Task task = new Task("Задача " + i, "Описание з. 1");
            int taskId = taskManager.addTask(task);
            expected.add(taskManager.getTask(taskId));
            Epic epic = new Epic("Эпик " + i, "Описание э. 1", null);
            int epicId = taskManager.addEpic(epic);
            taskManager.getEpic(epicId);
        }
        assertNotEquals(expected, taskManager.getHistory());
        taskManager.deleteEpics();
        assertEquals(expected, taskManager.getHistory());
        taskManager.deleteTasks();
        assertEquals(new ArrayList<>(), taskManager.getHistory());

        Epic epic2 = new Epic("Эпик 2", "Описание э. 2", null);
        epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        Subtask subtask3 = new Subtask(epic2Id, "Подзадача 3", "Описание п. 3");
        subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);
        taskManager.getSubtask(subtask1Id);
        taskManager.getSubtask(subtask2Id);
        taskManager.getSubtask(subtask3Id);
        assertEquals(List.of(subtask1, subtask2, subtask3), taskManager.getHistory());
        taskManager.deleteEpic(epic1Id);
        assertEquals(List.of(subtask3), taskManager.getHistory());
        taskManager.deleteEpics();
        assertEquals(new ArrayList<>(), taskManager.getHistory());
    }

    @Test
    public void getHistory() {
        assertEquals(new ArrayList<>(), taskManager.getHistory());
        taskManager.getTask(0);
        taskManager.getEpic(0);
        taskManager.getSubtask(0);
        assertEquals(new ArrayList<>(), taskManager.getHistory());
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        taskManager.getTasks();
        assertEquals(new ArrayList<>(), taskManager.getHistory());
        taskManager.getTask(task2Id);
        taskManager.getTask(task2Id);
        taskManager.getTask(task1Id);
        assertEquals(List.of(task2, task1), taskManager.getHistory());
    }
}
