package ru.icecubenext.kanban.managers.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.icecubenext.kanban.managers.TaskManager;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;
import ru.icecubenext.kanban.model.enums.Status;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager> {
    private T taskManager;

    public void setTaskManager(T taskManager) {
        this.taskManager = taskManager;
    }

    @AfterEach
    public void clearData() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
    }

    @Test
    void addTask() {
        assertEquals(0, taskManager.getTasks().size());
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        Task task3 = new Task("Задача 3", "Описание з. 3");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        int task3Id = taskManager.addTask(task3);
        assertEquals(3, taskManager.getTasks().size());
        assertEquals(task1, taskManager.getTask(task1Id));
        assertEquals(task2, taskManager.getTask(task2Id));
        assertEquals(task3, taskManager.getTask(task3Id));
        int task1repeat = taskManager.addTask(task1);
        assertEquals(-1, task1repeat);
        assertEquals(3, taskManager.getTasks().size());
        assertEquals(task1, taskManager.getTask(task1Id));
    }

    @Test
    public void addEpic() {
        assertEquals(0, taskManager.getEpics().size());
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(epic1, taskManager.getEpic(epic1Id));
        assertEquals(epic2, taskManager.getEpic(epic2Id));
        int epic1repeat = taskManager.addEpic(epic1);
        assertEquals(-1, epic1repeat);
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(epic1, taskManager.getEpic(epic1Id));
    }

    @Test
    public void addSubtask() {
        assertEquals(0, taskManager.getSubtasks().size());
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
        assertEquals(3, taskManager.getSubtasks().size());
        assertEquals(subtask1, taskManager.getSubtask(subtask1Id));
        assertEquals(subtask2, taskManager.getSubtask(subtask2Id));
        assertEquals(subtask3, taskManager.getSubtask(subtask3Id));
        assertEquals(2, taskManager.getEpic(epic1Id).getSubtasks().size());
        assertEquals(1, taskManager.getEpic(epic2Id).getSubtasks().size());
        List<Subtask> actual = taskManager.getEpic(epic1Id).getSubtasks();
        List<Subtask> expected = new ArrayList<>();
        expected.add(subtask1);
        expected.add(subtask2);
        assertEquals(expected, actual);
        actual = taskManager.getEpic(epic2Id).getSubtasks();
        expected.clear();
        expected.add(subtask3);
        assertEquals(expected, actual);
        int subtask1repeat = taskManager.addSubtask(subtask1);
        assertEquals(-1, subtask1repeat);
        assertEquals(3, taskManager.getSubtasks().size());
        assertEquals(subtask1, taskManager.getSubtask(subtask1Id));
    }

    @Test
    public void getTasks() {
        assertNotNull(taskManager.getTasks());
        assertEquals(new ArrayList<Task>(), taskManager.getTasks());
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size());
        assertEquals(Arrays.asList(task1, task2), taskManager.getTasks());
        assertNotEquals(Arrays.asList(task2, task1), taskManager.getTasks());
    }

    @Test
    public void getEpics() {
        assertNotNull(taskManager.getEpics());
        assertEquals(new ArrayList<Epic>(), taskManager.getEpics());
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(Arrays.asList(epic1, epic2), taskManager.getEpics());
        assertNotEquals(Arrays.asList(epic2, epic1), taskManager.getEpics());
    }

    @Test
    public void getSubtasks() {
        assertNotNull(taskManager.getSubtasks());
        assertEquals(new ArrayList<Subtask>(), taskManager.getSubtasks());
        Epic epic1 = new Epic("Эпик0", "Описание э. 0", null);
        Epic epic2 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask3 = new Subtask(epic2Id, "Подзадача 1", "Описание п. 1");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        assertEquals(Arrays.asList(subtask1, subtask2, subtask3), taskManager.getSubtasks());
        assertNotEquals(Arrays.asList(subtask2, subtask1, subtask3), taskManager.getSubtasks());
    }

    @Test
    public void getTask() {
        assertNull(taskManager.getTask(0));
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        Task task3 = new Task("Задача 3", "Описание з. 3");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        int task3Id = taskManager.addTask(task3);
        taskManager.deleteTask(task2Id);
        assertEquals(task1, taskManager.getTask(task1Id));
        assertNull(taskManager.getTask(task2Id));
        assertEquals(task3, taskManager.getTask(task3Id));

    }

    @Test
    public void getEpic() {
        assertNull(taskManager.getEpic(0));
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        Epic epic3 = new Epic("Эпик3", "Описание э. 3", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        int epic3Id = taskManager.addEpic(epic3);
        taskManager.deleteEpic(epic3Id);
        assertEquals(epic1, taskManager.getEpic(epic1Id));
        assertEquals(epic2, taskManager.getEpic(epic2Id));
        assertNull(taskManager.getEpic(epic3Id));
    }

    @Test
    public void getSubtask() {
        assertNull(taskManager.getSubtask(0));
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
        taskManager.deleteSubtask(subtask2Id);
        assertEquals(subtask1, taskManager.getSubtask(subtask1Id));
        assertEquals(subtask3, taskManager.getSubtask(subtask3Id));
        assertNull(taskManager.getSubtask(subtask2Id));
    }

    //TODO Проверить что при добавлении 2-х эпиков с одинаковыми данными оба добавляются
    @Test
    public void getEpicsSubtasks() {
        assertNotNull(taskManager.getEpicsSubtasks(0));
        assertEquals(new ArrayList<Subtask>(), taskManager.getEpicsSubtasks(0));
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic2Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        Subtask subtask3 = new Subtask(epic1Id, "Подзадача 3", "Описание п. 3");
        taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        assertEquals(2, taskManager.getEpicsSubtasks(epic1Id).size());
        assertEquals(1, taskManager.getEpicsSubtasks(epic2Id).size());
        assertEquals(List.of(subtask2, subtask3), taskManager.getEpicsSubtasks(epic1Id));
        assertEquals(List.of(subtask1), taskManager.getEpicsSubtasks(epic2Id));
        taskManager.deleteSubtask(subtask2Id);
        assertEquals(List.of(subtask3), taskManager.getEpicsSubtasks(epic1Id));
    }

    @Test
    public void updateTask() {
        Task task1 = new Task("Задача 1", "Описание з. 1");
        assertFalse(taskManager.updateTask(task1));
        int task1Id = taskManager.addTask(task1);
        assertEquals(1, taskManager.getTasks().size());
        assertEquals(Status.NEW, taskManager.getTask(task1Id).getStatus());
        assertEquals("Задача 1", taskManager.getTask(task1Id).getName());
        assertEquals("Описание з. 1", taskManager.getTask(task1Id).getDescription());
        Task updatableTask = new Task(task1Id, "Задача 2", "Описание з. 2");
        updatableTask.setStatus(Status.DONE);
        taskManager.updateTask(updatableTask);
        assertEquals(updatableTask, taskManager.getTask(task1Id));
        assertEquals(1, taskManager.getTasks().size());
        assertEquals(Status.DONE, taskManager.getTask(task1Id).getStatus());
        assertEquals("Задача 2", taskManager.getTask(task1Id).getName());
        assertEquals("Описание з. 2", taskManager.getTask(task1Id).getDescription());
    }

    @Test
    public void updateEpic() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        assertFalse(taskManager.updateEpic(epic1));
        int epic1Id = taskManager.addEpic(epic1);
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(Status.NEW, taskManager.getEpic(epic1Id).getStatus());
        assertEquals("Эпик1", taskManager.getEpic(epic1Id).getName());
        assertEquals("Описание э. 1", taskManager.getEpic(epic1Id).getDescription());
        Epic updatableEpic = new Epic(epic1Id, "Обновленный эпик", "Новое описание", null);
        taskManager.updateEpic(updatableEpic);
        assertEquals(updatableEpic, taskManager.getEpic(epic1Id));
        assertEquals(1, taskManager.getEpics().size());
        assertEquals("Обновленный эпик", taskManager.getEpic(epic1Id).getName());
        assertEquals("Новое описание", taskManager.getEpic(epic1Id).getDescription());
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 0", "Описание п. 0");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 3", "Описание п. 3");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        List<Subtask> subtasks= List.of(subtask1, subtask2);
        updatableEpic = new Epic(epic1Id, "Обновленный эпик 1", "Обновленное описание э. 1", subtasks);
        taskManager.updateEpic(updatableEpic);
        assertEquals(updatableEpic, taskManager.getEpic(epic1Id));
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(subtasks, taskManager.getEpicsSubtasks(epic1Id));
    }

    @Test
    public void updateSubtask() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        assertFalse(taskManager.updateSubtask(subtask1));
        int subtask1Id = taskManager.addSubtask(subtask1);
        assertEquals(1, taskManager.getSubtasks().size());
        assertEquals(Status.NEW, taskManager.getSubtask(subtask1Id).getStatus());
        assertEquals("Подзадача 1", taskManager.getSubtask(subtask1Id).getName());
        assertEquals("Описание п. 1", taskManager.getSubtask(subtask1Id).getDescription());
        Subtask updatableSubtask = new Subtask(subtask1Id, epic1Id, "Нов. подзадача", "Новое описание");
        updatableSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(updatableSubtask);
        assertEquals(updatableSubtask, taskManager.getSubtask(subtask1Id));
        assertEquals(1, taskManager.getSubtasks().size());
        assertEquals(Status.DONE, taskManager.getSubtask(subtask1Id).getStatus());
        assertEquals(Status.DONE, epic1.getStatus());
        assertEquals("Нов. подзадача", taskManager.getSubtask(subtask1Id).getName());
        assertEquals("Новое описание", taskManager.getSubtask(subtask1Id).getDescription());
    }

    @Test
    public void deleteTasks() {
        assertEquals(0, taskManager.getTasks().size());
        assertTrue(taskManager.deleteTasks());
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size());
        taskManager.deleteTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void deleteEpics() {
        assertEquals(0, taskManager.getEpics().size());
        assertTrue(taskManager.deleteEpics());
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик3", "Описание э. 3", null);
        int epic1Id = taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        assertEquals(2, taskManager.getEpics().size());
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        taskManager.addSubtask(subtask1);
        assertEquals(1, taskManager.getEpicsSubtasks(epic1Id).size());
        assertEquals(1, taskManager.getSubtasks().size());
        taskManager.deleteEpics();
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getEpicsSubtasks(epic1Id).size());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void deleteSubtasks() {
        assertEquals(0, taskManager.getSubtasks().size());
        assertTrue(taskManager.deleteSubtasks());
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        Subtask subtask3 = new Subtask(epic1Id, "Подзадача 3", "Описание п. 3");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        assertEquals(3, taskManager.getSubtasks().size());
        taskManager.deleteSubtasks();
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getEpicsSubtasks(epic1Id).size());
    }

    @Test
    public void deleteTask() {
        assertEquals(0, taskManager.getTasks().size());
        assertFalse(taskManager.deleteTask(0));
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size());
        taskManager.deleteTask(task1Id);
        assertEquals(List.of(task2), taskManager.getTasks());
        taskManager.deleteTask(task2Id);
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void deleteEpic() {
        assertEquals(0, taskManager.getEpics().size());
        assertFalse(taskManager.deleteEpic(0));
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        assertEquals(2, taskManager.getEpics().size());
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic2Id, "Подзадача 2", "Описание п. 2");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(1, taskManager.getEpicsSubtasks(epic1Id).size());
        assertEquals(1, taskManager.getEpicsSubtasks(epic2Id).size());
        assertEquals(2, taskManager.getSubtasks().size());
        taskManager.deleteEpic(epic1Id);
        assertEquals(List.of(epic2), taskManager.getEpics());
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(0, taskManager.getEpicsSubtasks(epic1Id).size());
        assertEquals(1, taskManager.getSubtasks().size());
        taskManager.deleteEpic(epic2Id);
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getEpicsSubtasks(epic2Id).size());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void deleteSubtask() {
        assertEquals(0, taskManager.getSubtasks().size());
        assertFalse(taskManager.deleteSubtask(0));
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        assertEquals(2, taskManager.getSubtasks().size());
        assertEquals(2, taskManager.getEpicsSubtasks(epic1Id).size());
        taskManager.deleteSubtask(subtask1Id);
        assertEquals(1, taskManager.getSubtasks().size());
        assertEquals(1, taskManager.getEpicsSubtasks(epic1Id).size());
        assertEquals(List.of(subtask2), taskManager.getSubtasks());
        taskManager.deleteSubtask(subtask2Id);
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getEpicsSubtasks(epic1Id).size());
    }

    @Test
    public void checkEpicStatus() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        assertEquals(Status.NEW, taskManager.getEpic(epic1Id).getStatus());
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        assertEquals(Status.NEW, taskManager.getSubtask(subtask1Id).getStatus());
        assertEquals(Status.NEW, taskManager.getSubtask(subtask2Id).getStatus());
        assertEquals(Status.NEW, taskManager.getEpic(epic1Id).getStatus());

        taskManager.getSubtask(subtask1Id).setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, taskManager.getSubtask(subtask1Id).getStatus());
        assertEquals(Status.NEW, taskManager.getSubtask(subtask2Id).getStatus());
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1Id).getStatus());

        taskManager.getSubtask(subtask2Id).setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, taskManager.getSubtask(subtask1Id).getStatus());
        assertEquals(Status.IN_PROGRESS, taskManager.getSubtask(subtask2Id).getStatus());
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1Id).getStatus());

        taskManager.getSubtask(subtask1Id).setStatus(Status.DONE);
        assertEquals(Status.DONE, taskManager.getSubtask(subtask1Id).getStatus());
        assertEquals(Status.IN_PROGRESS, taskManager.getSubtask(subtask2Id).getStatus());
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1Id).getStatus());

        taskManager.getSubtask(subtask2Id).setStatus(Status.DONE);
        assertEquals(Status.DONE, taskManager.getSubtask(subtask1Id).getStatus());
        assertEquals(Status.DONE, taskManager.getSubtask(subtask2Id).getStatus());
        assertEquals(Status.DONE, taskManager.getEpic(epic1Id).getStatus());

        taskManager.getSubtask(subtask1Id).setStatus(Status.NEW);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1Id).getStatus());
        taskManager.getSubtask(subtask2Id).setStatus(Status.NEW);
        assertEquals(Status.NEW, taskManager.getEpic(epic1Id).getStatus());
    }

    @Test
    public void checkDuration() {
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        Task task3 = new Task("Задача 3", "Описание з. 3");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        int task3Id = taskManager.addTask(task3);
        assertEquals(List.of(task1, task2, task3), taskManager.getPrioritizedTasks());

        LocalDateTime dateTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 30);
        LocalDateTime dateTime3 = LocalDateTime.of(2010, Month.JUNE, 10, 9, 0);
        Task updatableTask1 = new Task(task1Id, "Задача 1", "Описание", dateTime1, 0);
        Task updatableTask2 = new Task(task2Id, "Задача 2", "Описание", dateTime2, 0);
        Task updatableTask3 = new Task(task3Id, "Задача 3", "Описание", dateTime3, 0);
        updatableTask1.setDuration(0);
        updatableTask2.setDuration(0);
        updatableTask3.setDuration(0);
        taskManager.updateTask(updatableTask1);
        taskManager.updateTask(updatableTask2);
        taskManager.updateTask(updatableTask3);
        task1 = taskManager.getTask(task1Id);
        task2 = taskManager.getTask(task2Id);
        task3 = taskManager.getTask(task3Id);
        assertEquals(List.of(task1, task2, task3), taskManager.getPrioritizedTasks());
        updatableTask1.setDuration(1);
        taskManager.updateTask(updatableTask1);
        assertEquals(List.of(task1, task2, task3), taskManager.getPrioritizedTasks());
        updatableTask2.setDuration(1);
        taskManager.updateTask(updatableTask2);
        assertEquals(List.of(task1, task2, task3), taskManager.getPrioritizedTasks());
        updatableTask3.setDuration(10);
        task3 = taskManager.getTask(task3Id);
        assertEquals(List.of(task1, task2, task3), taskManager.getPrioritizedTasks());

        Epic epic1 = new Epic("Эпик0", "Описание э. 0", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        Subtask subtask3 = new Subtask(epic1Id, "Подзадача 3", "Описание п. 3");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);
        assertNull(taskManager.getEpic(epic1Id).getStartTime());
        Subtask updatableSubtask1 = new Subtask(subtask1Id, epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask updatableSubtask2 = new Subtask(subtask2Id, epic1Id, "Подзадача 2", "Описание п. 2");
        Subtask updatableSubtask3 = new Subtask(subtask3Id, epic1Id, "Подзадача 3", "Описание п. 3");
        LocalDateTime subtask1ld = LocalDateTime.of(2010, Month.JUNE, 10, 10, 0);
        LocalDateTime subtask2ld = LocalDateTime.of(2010, Month.JUNE, 10, 9, 59);
        LocalDateTime subtask3ld = LocalDateTime.of(2010, Month.JUNE, 10, 10, 30);

        updatableSubtask1.setStartTime(subtask1ld);
        updatableSubtask1.setDuration(30);
        taskManager.updateSubtask(updatableSubtask1);
        assertEquals(subtask1ld, taskManager.getSubtask(subtask1Id).getStartTime());
        assertEquals(subtask1ld, taskManager.getEpic(epic1Id).getStartTime());
        assertEquals(LocalDateTime.of(2010, Month.JUNE, 10, 10, 30), taskManager.getEpic(epic1Id).getEndTime());

        updatableSubtask2.setStartTime(subtask2ld);
        updatableSubtask2.setDuration(1);
        taskManager.updateSubtask(updatableSubtask2);
        assertEquals(subtask2ld, taskManager.getSubtask(subtask2Id).getStartTime());
        assertEquals(subtask2ld, taskManager.getEpic(epic1Id).getStartTime());
        assertEquals(31, taskManager.getEpic(epic1Id).getDuration());
        assertEquals(LocalDateTime.of(2010, Month.JUNE, 10, 10, 30), taskManager.getEpic(epic1Id).getEndTime());

        updatableSubtask3.setStartTime(subtask3ld);
        updatableSubtask3.setDuration(30);
        taskManager.updateSubtask(updatableSubtask3);
        assertEquals(subtask2ld, taskManager.getEpic(epic1Id).getStartTime());
        assertEquals(LocalDateTime.of(2010, Month.JUNE, 10, 11, 0), taskManager.getEpic(epic1Id).getEndTime());
        assertEquals(61, taskManager.getEpic(epic1Id).getDuration());

        updatableSubtask3.setStartTime(LocalDateTime.of(2010, Month.JUNE, 10, 10, 29));
        updatableSubtask3.setDuration(30);
        taskManager.updateSubtask(updatableSubtask3);
        assertEquals(subtask2ld, taskManager.getEpic(epic1Id).getStartTime());
        assertEquals(LocalDateTime.of(2010, Month.JUNE, 10, 10, 59), taskManager.getEpic(epic1Id).getEndTime());

        updatableSubtask3.setStartTime(LocalDateTime.of(2010, Month.JUNE, 10, 11, 0));
        updatableSubtask3.setDuration(30);
        taskManager.updateSubtask(updatableSubtask3);
        assertEquals(subtask2ld, taskManager.getEpic(epic1Id).getStartTime());
        assertEquals(LocalDateTime.of(2010, Month.JUNE, 10, 11, 30), taskManager.getEpic(epic1Id).getEndTime());

        Subtask subtask4 = new Subtask(epic1Id, "Подзадача 4", "Описание п. 4");
        subtask4.setStartTime(LocalDateTime.of(2010, Month.JUNE, 10, 12, 59));
        taskManager.addSubtask(subtask4);
        assertEquals(LocalDateTime.of(2010, Month.JUNE, 10, 12, 59), taskManager.getEpic(epic1Id).getEndTime());
        assertEquals(LocalDateTime.of(2010, Month.JUNE, 10, 9, 59), taskManager.getEpic(epic1Id).getStartTime());
        assertEquals(180, taskManager.getEpic(epic1Id).getDuration());
    }
}