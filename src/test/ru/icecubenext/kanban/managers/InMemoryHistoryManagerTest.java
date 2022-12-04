package test.ru.icecubenext.kanban.managers;

import main.ru.icecubenext.kanban.managers.Manager;
import main.ru.icecubenext.kanban.managers.TaskManager;
import main.ru.icecubenext.kanban.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManagerTest {

    @Test
    public void add() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertEquals(0, taskManager.getTasks().size());
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Epic epic1 = new Epic("Эпик 1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        int task1Id = taskManager.addTask(task1);
        int subtask1Id = taskManager.addSubtask(subtask1);
        List<Task> expected = new ArrayList<>();

        Assert.assertEquals(0, taskManager.getHistory().size());
        taskManager.getTask(task1Id);
        expected.add(0, task1);
        Assert.assertEquals(1, taskManager.getHistory().size());
        taskManager.getTask(task1Id);
        expected.add(0, task1);
        Assert.assertEquals(2, taskManager.getHistory().size());
        taskManager.getEpic(epic1Id);
        expected.add(0, epic1);
        Assert.assertEquals(3, taskManager.getHistory().size());
        taskManager.getEpic(epic1Id);
        expected.add(0, epic1);
        Assert.assertEquals(4, taskManager.getHistory().size());
        taskManager.getSubtask(subtask1Id);
        expected.add(0, subtask1);
        Assert.assertEquals(5, taskManager.getHistory().size());
        taskManager.getSubtask(subtask1Id);
        expected.add(0, subtask1);
        Assert.assertEquals(6, taskManager.getHistory().size());
        Assert.assertEquals(expected, taskManager.getHistory());
    }

    @Test
    public void getHistory() {
        TaskManager taskManager = Manager.getDefault();
        for (int i = 0; i < 11; i++) {
            Task task = new Task("Задача " + i, "Описание з. 1");
            int taskId = taskManager.addTask(task);
            taskManager.getTask(taskId);
        }
        Assert.assertEquals(11, taskManager.getTasks().size());
        Assert.assertEquals(10, taskManager.getHistory().size());
    }
}