package ru.icecubenext.kanban.managers.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.icecubenext.kanban.managers.Manager;
import ru.icecubenext.kanban.managers.TaskManager;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;

import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = Manager.getDefault();
    }

    @AfterEach
    public void clearData() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
    }

    @Test
    public void addWithRepeats() {
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Epic epic1 = new Epic("Эпик 1", "Описание э. 1");
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
    public void removeFromHead() {
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Epic epic1 = new Epic("Эпик 1", "Описание э. 1");
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
    }

    @Test
    public void removeFromTail() {
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Epic epic1 = new Epic("Эпик 1", "Описание э. 1");
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        int task1Id = taskManager.addTask(task1);
        int subtask1Id = taskManager.addSubtask(subtask1);
        taskManager.getTask(task1Id);
        taskManager.getEpic(epic1Id);
        taskManager.getSubtask(subtask1Id);
        assertEquals(List.of(task1, epic1, subtask1), taskManager.getHistory());
        taskManager.deleteSubtask(subtask1Id);
        assertEquals(List.of(task1, epic1), taskManager.getHistory());
    }

    @Test
    public void removeFromMiddle() {
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        Task task3 = new Task("Задача 3", "Описание з. 3");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        int task3Id = taskManager.addTask(task3);
        taskManager.getTask(task1Id);
        taskManager.getTask(task2Id);
        taskManager.getTask(task3Id);
        assertEquals(List.of(task1, task2, task3), taskManager.getHistory());
        taskManager.deleteTask(task2Id);
        assertEquals(List.of(task1, task3), taskManager.getHistory());
    }

    @Test
    public void removeEpic() {
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Epic epic1 = new Epic("Эпик 1", "Описание э. 1");
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        int task1Id = taskManager.addTask(task1);
        int subtask1Id = taskManager.addSubtask(subtask1);
        taskManager.getTask(task1Id);
        taskManager.getEpic(epic1Id);
        taskManager.getSubtask(subtask1Id);
        assertEquals(List.of(task1, epic1, subtask1), taskManager.getHistory());
        taskManager.deleteEpic(epic1Id);
        assertEquals(List.of(task1), taskManager.getHistory());
    }

    @Test
    public void getHistory() {
        assertEquals(0, taskManager.getHistory().size());
        taskManager.getTask(0);
        taskManager.getEpic(-1);
        taskManager.getSubtask(1);
        assertEquals(0, taskManager.getHistory().size());
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        taskManager.getTasks();
        assertEquals(0, taskManager.getHistory().size());
        taskManager.getTask(task2Id);
        taskManager.getTask(task2Id);
        taskManager.getTask(task1Id);
        assertEquals(List.of(task2, task1), taskManager.getHistory());
    }
}
