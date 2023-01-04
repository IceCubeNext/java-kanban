package test.ru.icecubenext.kanban.managers;

import main.ru.icecubenext.kanban.managers.Manager;
import main.ru.icecubenext.kanban.model.*;
import main.ru.icecubenext.kanban.managers.*;
import main.ru.icecubenext.kanban.model.enums.Status;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManagerTest {

    @org.junit.Test
    public void addTask() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertEquals(0, taskManager.getTasks().size());
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        Task task3 = new Task("Задача 3", "Описание з. 3");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        int task3Id = taskManager.addTask(task3);
        Assert.assertEquals(3, taskManager.getTasks().size());
        Assert.assertEquals(task1, taskManager.getTask(task1Id));
        Assert.assertEquals(task2, taskManager.getTask(task2Id));
        Assert.assertEquals(task3, taskManager.getTask(task3Id));
        int task1repeat = taskManager.addTask(task1);
        Assert.assertEquals(-1, task1repeat);
        Assert.assertEquals(3, taskManager.getTasks().size());
        Assert.assertEquals(task1, taskManager.getTask(task1Id));
    }

    @org.junit.Test
    public void addEpic() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertEquals(0, taskManager.getEpics().size());
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Assert.assertEquals(2, taskManager.getEpics().size());
        Assert.assertEquals(epic1, taskManager.getEpic(epic1Id));
        Assert.assertEquals(epic2, taskManager.getEpic(epic2Id));
        int epic1repeat = taskManager.addEpic(epic1);
        Assert.assertEquals(-1, epic1repeat);
        Assert.assertEquals(2, taskManager.getEpics().size());
        Assert.assertEquals(epic1, taskManager.getEpic(epic1Id));
    }

    @org.junit.Test
    public void addSubtask() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertEquals(0, taskManager.getSubtasks().size());
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        Subtask subtask3 = new Subtask(epic2Id, "Подзадача 3", "Описание п. 3");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);
        Assert.assertEquals(3, taskManager.getSubtasks().size());
        Assert.assertEquals(subtask1, taskManager.getSubtask(subtask1Id));
        Assert.assertEquals(subtask2, taskManager.getSubtask(subtask2Id));
        Assert.assertEquals(subtask3, taskManager.getSubtask(subtask3Id));
        Assert.assertEquals(2, taskManager.getEpic(epic1Id).getSubtasks().size());
        Assert.assertEquals(1, taskManager.getEpic(epic2Id).getSubtasks().size());
        List<Subtask> actual = taskManager.getEpic(epic1Id).getSubtasks();
        List<Subtask> expected = new ArrayList<>();
        expected.add(subtask1);
        expected.add(subtask2);
        Assert.assertEquals(expected, actual);
        actual = taskManager.getEpic(epic2Id).getSubtasks();
        expected.clear();
        expected.add(subtask3);
        Assert.assertEquals(expected, actual);
        int subtask1repeat = taskManager.addSubtask(subtask1);
        Assert.assertEquals(-1, subtask1repeat);
        Assert.assertEquals(3, taskManager.getSubtasks().size());
        Assert.assertEquals(subtask1, taskManager.getSubtask(subtask1Id));
    }

    @org.junit.Test
    public void getTasks() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertNotNull(taskManager.getTasks());
        Assert.assertEquals(new ArrayList<Task>(), taskManager.getTasks());
        Task task1 = new Task("Задача 1", "Описание з. 1");
        int task1Id = taskManager.addTask(task1);
        Assert.assertEquals(1, taskManager.getTasks().size());
        Assert.assertEquals(task1, taskManager.getTask(task1Id));
    }

    @org.junit.Test
    public void getEpics() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertNotNull(taskManager.getEpics());
        Assert.assertEquals(new ArrayList<Epic>(), taskManager.getEpics());
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Assert.assertEquals(1, taskManager.getEpics().size());
        Assert.assertEquals(epic1, taskManager.getEpic(epic1Id));
    }

    @org.junit.Test
    public void getSubtasks() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertNotNull(taskManager.getSubtasks());
        Assert.assertEquals(new ArrayList<Subtask>(), taskManager.getSubtasks());
        Epic epic1 = new Epic("Эпик0", "Описание э. 0", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        int subtask1Id = taskManager.addSubtask(subtask1);
        Assert.assertEquals(1, taskManager.getSubtasks().size());
        Assert.assertEquals(subtask1, taskManager.getSubtask(subtask1Id));
    }

    @org.junit.Test
    public void getTask() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertNull(taskManager.getTask(0));
        Task task1 = new Task("Задача 1", "Описание з. 1");
        int task1Id = taskManager.addTask(task1);
        Assert.assertEquals(1, taskManager.getTasks().size());
        Assert.assertEquals(task1, taskManager.getTask(task1Id));
    }

    @org.junit.Test
    public void getEpic() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertNull(taskManager.getEpic(0));
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Assert.assertEquals(epic1, taskManager.getEpic(epic1Id));
    }

    @org.junit.Test
    public void getSubtask() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertNull(taskManager.getSubtask(0));
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        int subtask1Id = taskManager.addSubtask(subtask1);
        Assert.assertEquals(subtask1, taskManager.getSubtask(subtask1Id));
    }

    @org.junit.Test
    public void getEpicsSubtasks() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertNotNull(taskManager.getEpicsSubtasks(0));
        Assert.assertEquals(new ArrayList<Subtask>(), taskManager.getEpicsSubtasks(0));
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        taskManager.addSubtask(subtask1);
        Assert.assertEquals(1, taskManager.getEpicsSubtasks(epic1Id).size());
        ArrayList<Subtask> expected = new ArrayList<>();
        expected.add(subtask1);
        Assert.assertEquals(expected, taskManager.getEpicsSubtasks(epic1Id));
    }

    @org.junit.Test
    public void updateTask() {
        TaskManager taskManager = Manager.getDefault();
        Task task1 = new Task("Задача 1", "Описание з. 1");
        int task1Id = taskManager.addTask(task1);
        Assert.assertEquals(1, taskManager.getTasks().size());
        Task updatableTask = new Task(task1Id, "Задача 2", "Описание з. 2");
        updatableTask.setStatus(Status.DONE);
        taskManager.updateTask(updatableTask);
        Assert.assertEquals(updatableTask, taskManager.getTask(task1Id));
        Assert.assertEquals(1, taskManager.getTasks().size());
    }

    @org.junit.Test
    public void updateEpic() {
        TaskManager taskManager = Manager.getDefault();
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Assert.assertEquals(1, taskManager.getEpics().size());
        Epic updatableEpic = new Epic(epic1Id, "Обновленный эпик", "Новое описание", null);
        taskManager.updateEpic(updatableEpic);
        Assert.assertEquals(updatableEpic, taskManager.getEpic(epic1Id));
        Assert.assertEquals(1, taskManager.getEpics().size());
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 0", "Описание п. 0");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 3", "Описание п. 3");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(taskManager.getSubtask(subtask1Id));
        subtasks.add(taskManager.getSubtask(subtask2Id));
        updatableEpic = new Epic(epic1Id, "Обновленный эпик 1", "Обновленное описание э. 1", subtasks);
        taskManager.updateEpic(updatableEpic);
        Assert.assertEquals(updatableEpic, taskManager.getEpic(epic1Id));
        Assert.assertEquals(1, taskManager.getEpics().size());
    }

    @org.junit.Test
    public void updateSubtask() {
        TaskManager taskManager = Manager.getDefault();
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        int subtask1Id = taskManager.addSubtask(subtask1);
        Assert.assertEquals(1, taskManager.getSubtasks().size());
        Subtask updatableSubtask = new Subtask(subtask1Id, epic1Id, "Нов. подзадача", "Новое описание");
        updatableSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(updatableSubtask);
        Assert.assertEquals(updatableSubtask, taskManager.getSubtask(subtask1Id));
        Assert.assertEquals(Status.DONE, epic1.getStatus());
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        int subtask2Id = taskManager.addSubtask(subtask2);
        Assert.assertEquals(2, epic1.getSubtasks().size());
        Assert.assertEquals(Status.IN_PROGRESS, epic1.getStatus());
        subtask1 = taskManager.getSubtask(subtask1Id);
        subtask2 = taskManager.getSubtask(subtask2Id);
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);
        Assert.assertEquals(Status.NEW, epic1.getStatus());
    }

    @org.junit.Test
    public void deleteTasks() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertEquals(0, taskManager.getTasks().size());
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        Assert.assertEquals(2, taskManager.getTasks().size());
        taskManager.deleteTasks();
        Assert.assertEquals(0, taskManager.getTasks().size());
    }

    @org.junit.Test
    public void deleteEpics() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertEquals(0, taskManager.getEpics().size());
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик3", "Описание э. 3", null);
        int epic1Id = taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        Assert.assertEquals(2, taskManager.getEpics().size());
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        taskManager.addSubtask(subtask1);
        Assert.assertEquals(1, taskManager.getEpicsSubtasks(epic1Id).size());
        Assert.assertEquals(1, taskManager.getSubtasks().size());
        taskManager.deleteEpics();
        Assert.assertEquals(0, taskManager.getEpics().size());
        Assert.assertEquals(0, taskManager.getEpicsSubtasks(epic1Id).size());
        Assert.assertEquals(0, taskManager.getSubtasks().size());
    }

    @org.junit.Test
    public void deleteSubtasks() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertEquals(0, taskManager.getSubtasks().size());
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        Subtask subtask3 = new Subtask(epic1Id, "Подзадача 3", "Описание п. 3");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        Assert.assertEquals(3, taskManager.getSubtasks().size());
        taskManager.deleteSubtasks();
        Assert.assertEquals(0, taskManager.getSubtasks().size());
        Assert.assertEquals(0, taskManager.getEpicsSubtasks(epic1Id).size());
    }

    @org.junit.Test
    public void deleteTask() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertEquals(0, taskManager.getTasks().size());
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        Assert.assertEquals(2, taskManager.getTasks().size());
        taskManager.deleteTask(task1Id);
        Assert.assertEquals(1, taskManager.getTasks().size());
        ArrayList<Task> expected = new ArrayList<>();
        expected.add(task2);
        Assert.assertEquals(expected, taskManager.getTasks());
        taskManager.deleteTask(task2Id);
        Assert.assertEquals(0, taskManager.getTasks().size());
    }

    @org.junit.Test
    public void deleteEpic() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertEquals(0, taskManager.getEpics().size());
        Epic epic1 = new Epic("Эпик4", "Описание э. 4", null);
        Epic epic2 = new Epic("Эпик5", "Описание э. 5", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Assert.assertEquals(2, taskManager.getEpics().size());
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic2Id, "Подзадача 2", "Описание п. 2");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        Assert.assertEquals(2, taskManager.getEpics().size());
        Assert.assertEquals(1, taskManager.getEpicsSubtasks(epic1Id).size());
        Assert.assertEquals(1, taskManager.getEpicsSubtasks(epic2Id).size());
        Assert.assertEquals(2, taskManager.getSubtasks().size());
        taskManager.deleteEpic(epic1Id);
        Assert.assertEquals(epic2, taskManager.getEpics().get(0));
        Assert.assertEquals(1, taskManager.getEpics().size());
        Assert.assertEquals(0, taskManager.getEpicsSubtasks(epic1Id).size());
        Assert.assertEquals(1, taskManager.getSubtasks().size());
        taskManager.deleteEpic(epic2Id);
        Assert.assertEquals(0, taskManager.getEpics().size());
        Assert.assertEquals(0, taskManager.getEpicsSubtasks(epic2Id).size());
        Assert.assertEquals(0, taskManager.getSubtasks().size());
    }

    @org.junit.Test
    public void deleteSubtask() {
        TaskManager taskManager = Manager.getDefault();
        Assert.assertEquals(0, taskManager.getSubtasks().size());
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 0", "Описание п. 0");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        Assert.assertEquals(2, taskManager.getSubtasks().size());
        Assert.assertEquals(2, taskManager.getEpicsSubtasks(epic1Id).size());
        taskManager.deleteSubtask(subtask1Id);
        Assert.assertEquals(1, taskManager.getSubtasks().size());
        Assert.assertEquals(1, taskManager.getEpicsSubtasks(epic1Id).size());
        Assert.assertEquals(subtask2, taskManager.getSubtasks().get(0));
        taskManager.deleteSubtask(subtask2Id);
        Assert.assertEquals(0, taskManager.getSubtasks().size());
        Assert.assertEquals(0, taskManager.getEpicsSubtasks(epic1Id).size());
    }
}