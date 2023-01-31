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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager> {
    private T taskManager;

    public void setTaskManager(T taskManager) {
        this.taskManager = taskManager;
    }
    public T getTaskManager() {
        return this.taskManager;
    }

    @AfterEach
    public void clearData() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
    }

    @Test
    void addTask() {
        assertEquals(0, taskManager.getTasks().size(), "менеджер не пуст при инициализации");
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        assertNotEquals(task1Id, task2Id, "менеджер возвращает одинаковые id");
        assertEquals(task1, taskManager.getTask(task1Id), "задачи не совпадают");
        assertEquals(task2, taskManager.getTask(task2Id), "задачи не совпадают");
        int task1repeat = taskManager.addTask(task1);
        assertEquals(-1, task1repeat, "добавляются одинаковые задачи");
    }

    @Test
    public void addEpic() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        assertNotEquals(epic1Id, epic2Id, "менеджер возвращает одинаковые id");
        assertEquals(epic1, taskManager.getEpic(epic1Id), "эпики не совпадают");
        assertEquals(epic2, taskManager.getEpic(epic2Id), "эпики не совпадают");
        int epic1repeat = taskManager.addEpic(epic1);
        assertEquals(-1, epic1repeat, "добавляются одинаковые эпики");
    }
    
    @Test
    public void addSubtask() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        assertNotEquals(subtask1Id, subtask2Id, "менджер возвращает одинаковые id");
        assertEquals(subtask1, taskManager.getSubtask(subtask1Id), "подзадачи не совпадают");
        assertEquals(subtask2, taskManager.getSubtask(subtask2Id), "подзадачи не совпадают");
        int subtask1repeat = taskManager.addSubtask(subtask1);
        assertEquals(-1, subtask1repeat, "добавляются одинаковые подзадачи");
    }

    @Test
    public void getTasks() {
        assertNotNull(taskManager.getTasks(), "не возвращается пустой контейнер");
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size(), "неверное количество подзадач");
        assertEquals(List.of(task1, task2), taskManager.getTasks(), "задачи не совпадают");
    }

    @Test
    public void getEpics() {
        assertNotNull(taskManager.getEpics(), "не возвращается пустой контейнер");
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        assertEquals(2, taskManager.getEpics().size(), "неверное количество эпиков");
        assertEquals(List.of(epic1, epic2), taskManager.getEpics(), "эпики не совпадают");
    }

    @Test
    public void getSubtasks() {
        assertNotNull(taskManager.getSubtasks(), "не возвращается пустой контейнер");
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(2, taskManager.getSubtasks().size(), "неверное количество подзадач");
        assertEquals(List.of(subtask1, subtask2), taskManager.getSubtasks(), "подзадачи не совпадают");
    }

    @Test
    public void getEpicsSubtasks() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        int epic1Id = taskManager.addEpic(epic1);
        assertNotNull(taskManager.getEpic(epic1Id).getSubtasks(), "не возвращается пустой контейнер");
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(2, taskManager.getEpic(epic1Id).getSubtasks().size(), "неверное количество подзадач");
    }

    @Test
    public void checkEpicsSubtasks() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        Subtask subtask3 = new Subtask(epic2Id, "Подзадача 3", "Описание п. 3");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        assertEquals(List.of(subtask1, subtask2), taskManager.getEpic(epic1Id).getSubtasks(), "неверный список подзадач");
        assertEquals(List.of(subtask3), taskManager.getEpic(epic2Id).getSubtasks(), "неверный список подзадач");
    }

    @Test
    public void checkSubtasksIdentity() {
        Epic epic = new Epic("Эпик1", "Описание э. 1");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask(epicId, "Подзадача 1", "Описание п. 1");
        int subtaskId = taskManager.addSubtask(subtask);
        assertEquals("Подзадача 1", taskManager.getSubtask(subtaskId).getName());
        assertEquals("Подзадача 1",taskManager.getEpic(epicId).getSubtasks().get(0).getName());
        taskManager.getSubtask(subtaskId).setName("Подзадача 2");
        assertEquals("Подзадача 2",taskManager.getEpic(epicId).getSubtasks().get(0).getName());
    }

    @Test
    public void getTask() {
        assertNull(taskManager.getTask(-1), "ошибка при запросе несуществующей задачи");
        assertNull(taskManager.getTask(0), "ошибка при запросе несуществующей задачи");
        assertNull(taskManager.getTask(1), "ошибка при запросе несуществующей задачи");
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        assertEquals(task1, taskManager.getTask(task1Id), "задачи не равны");
        assertEquals(task2, taskManager.getTask(task2Id), "задачи не равны");
    }

    @Test
    public void getEpic() {
        assertNull(taskManager.getEpic(-1), "ошибка при запросе несуществующего эпика");
        assertNull(taskManager.getEpic(0), "ошибка при запросе несуществующего эпика");
        assertNull(taskManager.getEpic(1), "ошибка при запросе несуществующего эпика");
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        assertEquals(epic1, taskManager.getEpic(epic1Id), "эпики не равны");
        assertEquals(epic2, taskManager.getEpic(epic2Id), "эпики не равны");
    }

    @Test
    public void getSubtask() {
        assertNull(taskManager.getSubtask(-1), "ошибка при запросе несуществующей подзадачи");
        assertNull(taskManager.getSubtask(0), "ошибка при запросе несуществующей подзадачи");
        assertNull(taskManager.getSubtask(1), "ошибка при запросе несуществующей подзадачи");
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic2Id, "Подзадача 2", "Описание п. 2");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        assertEquals(subtask1, taskManager.getSubtask(subtask1Id), "подзадачи не равны");
        assertEquals(subtask2, taskManager.getSubtask(subtask2Id), "подзадачи не равны");
    }

    @Test
    public void updateTask() {
        Task task1 = new Task("Задача 1", "Описание з. 1");
        assertFalse(taskManager.updateTask(task1), "обновление несуществующей задачи");
        int task1Id = taskManager.addTask(task1);
        Task updatableTask = new Task(task1Id, "Задача 2", "Описание з. 2");
        updatableTask.setStatus(Status.DONE);
        taskManager.updateTask(updatableTask);
        assertEquals(updatableTask, taskManager.getTask(task1Id), "задачи не равны");
        Task task2 = new Task("Задача 3", "Описание з. 3");
        assertFalse(taskManager.updateTask(task2), "обновление несуществующей задачи");
    }

    @Test
    public void updateEpic() {
        Epic epic = new Epic("Эпик1", "Описание э. 1");
        assertFalse(taskManager.updateEpic(epic), "обновление несуществующего эпика");
        int epicId = taskManager.addEpic(epic);
        Epic updatableEpic = new Epic(epicId, "Обновленный эпик", "Новое описание");
        taskManager.updateEpic(updatableEpic);
        assertEquals(updatableEpic, taskManager.getEpic(epicId), "эпики не равны");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
        assertFalse(taskManager.updateEpic(epic2), "обновление несуществующего эпика");
    }

    @Test
    public void updateSubtask() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        assertFalse(taskManager.updateSubtask(subtask1), "обновление несуществующей подзадачи");
        int subtask1Id = taskManager.addSubtask(subtask1);
        Subtask updatableSubtask = new Subtask(subtask1Id, epic1Id, "Нов. подзадача", "Новое описание");
        updatableSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(updatableSubtask);
        assertEquals(updatableSubtask, taskManager.getSubtask(subtask1Id));
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        assertFalse(taskManager.updateSubtask(subtask2), "обновление несуществующей подзадачи");
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
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
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
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic2Id, "Подзадача 2", "Описание п. 2");
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(2, taskManager.getSubtasks().size());
        assertEquals(1, taskManager.getEpicsSubtasks(epic1Id).size());
        assertEquals(1, taskManager.getEpicsSubtasks(epic2Id).size());
        taskManager.deleteSubtasks();
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getEpicsSubtasks(epic1Id).size());
        assertEquals(0, taskManager.getEpicsSubtasks(epic2Id).size());
    }

    @Test
    public void deleteTask() {
        assertEquals(0, taskManager.getTasks().size());
        assertFalse(taskManager.deleteTask(-1), "удаление несуществующей задачи");
        assertFalse(taskManager.deleteTask(0), "удаление несуществующей задачи");
        assertFalse(taskManager.deleteTask(1), "удаление несуществующей задачи");
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size());
        taskManager.deleteTask(task1Id);
        assertEquals(List.of(task2), taskManager.getTasks());
        assertFalse(taskManager.deleteTask(-1), "удаление несуществующей задачи");
        assertFalse(taskManager.deleteTask(task1Id), "удаление несуществующей задачи");
        taskManager.deleteTask(task2Id);
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void deleteEpic() {
        assertEquals(0, taskManager.getEpics().size());
        assertFalse(taskManager.deleteEpic(-1), "удаление несуществующего эпика");
        assertFalse(taskManager.deleteEpic(0), "удаление несуществующего эпика");
        assertFalse(taskManager.deleteEpic(1), "удаление несуществующего эпика");
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        Epic epic2 = new Epic("Эпик2", "Описание э. 2");
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
        assertEquals(1, taskManager.getSubtasks().size());
        assertFalse(taskManager.deleteEpic(epic1Id), "удаление несуществующего эпика");
        assertFalse(taskManager.deleteEpic(-1), "удаление несуществующего эпика");
        taskManager.deleteEpic(epic2Id);
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void deleteSubtask() {
        assertEquals(0, taskManager.getSubtasks().size());
        assertFalse(taskManager.deleteSubtask(-1),"удаление несуществующей подзадачи");
        assertFalse(taskManager.deleteSubtask(0),"удаление несуществующей подзадачи");
        assertFalse(taskManager.deleteSubtask(1),"удаление несуществующей подзадачи");
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
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
        assertFalse(taskManager.deleteSubtask(-1),"удаление несуществующей подзадачи");
        assertFalse(taskManager.deleteSubtask(subtask1Id),"удаление несуществующей подзадачи");
        taskManager.deleteSubtask(subtask2Id);
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getEpicsSubtasks(epic1Id).size());
    }

    @Test
    public void checkEmptyEpicStatus() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        int epic1Id = taskManager.addEpic(epic1);
        assertEquals(Status.NEW, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");
        taskManager.getEpic(epic1Id).setStatus(Status.IN_PROGRESS);
        assertEquals(Status.NEW, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");
        taskManager.getEpic(epic1Id).setStatus(Status.DONE);
        assertEquals(Status.NEW, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");
    }

    @Test
    public void checkUpgradeEpicStatus() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        assertEquals(Status.NEW, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");

        taskManager.getSubtask(subtask1Id).setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");
        taskManager.getSubtask(subtask2Id).setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");

        taskManager.getSubtask(subtask1Id).setStatus(Status.DONE);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");
        taskManager.getSubtask(subtask2Id).setStatus(Status.DONE);
        assertEquals(Status.DONE, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");
    }

    @Test
    public void checkDowngradeEpicStatus() {
        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        taskManager.getSubtask(subtask1Id).setStatus(Status.DONE);
        taskManager.getSubtask(subtask2Id).setStatus(Status.DONE);
        assertEquals(Status.DONE, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");

        taskManager.getSubtask(subtask1Id).setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");
        taskManager.getSubtask(subtask2Id).setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");

        taskManager.getSubtask(subtask1Id).setStatus(Status.NEW);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");
        taskManager.getSubtask(subtask2Id).setStatus(Status.NEW);
        assertEquals(Status.NEW, taskManager.getEpic(epic1Id).getStatus(), "статус не совпадает");
    }

    @Test
    public void checkTaskAddableWithoutStartTime() {
        Task task1 = new Task(0, "Задача 1", "Описание з. 1", null,-1);
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", null,0);
        Task task3 = new Task(0, "Задача 3", "Описание з. 3", null,1);
        Task task4 = new Task("Задача 4", "Описание з. 4");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);

        Epic epic1 = new Epic("Эпик1", "Описание э. 1");
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(0, epic1Id, "Подзадача 1", "Описание п. 1", null, -1);
        Subtask subtask2 = new Subtask(0, epic1Id, "Подзадача 2", "Описание п. 2", null, 0);
        Subtask subtask3 = new Subtask(0, epic1Id, "Подзадача 3", "Описание п. 3", null, 1);
        Subtask subtask4 = new Subtask(epic1Id,"Подзадача 4", "Описание п. 4");

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);
        assertEquals(4, taskManager.getTasks().size(), "количество задач не совпадает");
        assertEquals(4, taskManager.getSubtasks().size(), "количество подзадач не совпадает");
    }

    @Test
    public void checkConsequentiallyDuration() {
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 30);
        LocalDateTime startTime3 = LocalDateTime.of(2010, Month.JUNE, 10, 9, 0);
        LocalDateTime startTime4 = LocalDateTime.of(2010, Month.JUNE, 10, 9, 30);
        Task task1 = new Task(0, "Задача 1", "Описание з. 1", startTime1,30);
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", startTime2,30);
        Task task3 = new Task(0, "Задача 3", "Описание з. 3", startTime4,30);
        Epic epic = new Epic("Эпик1", "Описание э. 1");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask(0, epicId, "Подзадача 1", "Описание п. 1", startTime3, 30);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addSubtask(subtask);
        assertEquals(3, taskManager.getTasks().size(), "количество задач не совпадает");
        assertEquals(1, taskManager.getSubtasks().size(), "количество задач не совпадает");
        assertEquals(List.of(task1, task2, subtask, task3), taskManager.getPrioritizedTasks(), "порядок задач не совпадает");

        LocalDateTime startTime5 = LocalDateTime.of(2010, Month.JUNE, 10, 7, 30);
        Task task4 = new Task(0, "Задача 4", "Описание з. 4", startTime5,31);
        taskManager.addTask(task4);
        assertEquals(3, taskManager.getTasks().size(), "количество задач не совпадает");
        assertEquals(List.of(task1, task2, subtask, task3), taskManager.getPrioritizedTasks(), "порядок задач не совпадает");
    }

    @Test
    public void checkSimultaneousStart() {
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime3 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        Task task1 = new Task(0, "Задача 1", "Описание з. 1", startTime1,0);
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", startTime2,0);
        Epic epic = new Epic("Эпик1", "Описание э. 1");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask(0, epicId, "Подзадача", "Описание", startTime3, 0);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addSubtask(subtask);
        assertEquals(1, taskManager.getTasks().size(), "неверное число задач");
        assertEquals(0, taskManager.getSubtasks().size(), "неверное число подзадач");
        assertEquals(List.of(task1), taskManager.getPrioritizedTasks(), "порядок задач не совпадает");
    }

    @Test
    public void checkCrossingTimeIntervalsBelow() {
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2010, Month.JUNE, 10, 7, 50);
        Task task1 = new Task(0, "Задача 1", "Описание з. 1", startTime1,11);
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", startTime2,11);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(1, taskManager.getTasks().size(), "неверное число задач");
        assertEquals(List.of(task1), taskManager.getPrioritizedTasks(), "порядок задач не совпадает");

        Epic epic = new Epic("Эпик1", "Описание э. 1");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask(0, epicId, "Подзадача", "Описание", startTime2, 11);
        taskManager.addSubtask(subtask);
        assertEquals(1, taskManager.getTasks().size(), "неверное число задач");
        assertEquals(0, taskManager.getSubtasks().size(), "неверное число подзадач");
        assertEquals(List.of(task1), taskManager.getPrioritizedTasks(), "порядок задач не совпадает");
    }

    @Test
    public void checkCrossingTimeIntervalsAbove() {
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 10);
        Task task1 = new Task(0, "Задача 1", "Описание з. 1", startTime1,11);
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", startTime2,11);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(1, taskManager.getTasks().size(), "неверное число задач");
        assertEquals(List.of(task1), taskManager.getPrioritizedTasks(), "порядок задач не совпадает");

        Epic epic = new Epic("Эпик1", "Описание э. 1");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask(0, epicId, "Подзадача", "Описание", startTime2, 11);
        taskManager.addSubtask(subtask);
        assertEquals(1, taskManager.getTasks().size(), "неверное число задач");
        assertEquals(0, taskManager.getSubtasks().size(), "неверное число подзадач");
        assertEquals(List.of(task1), taskManager.getPrioritizedTasks(), "порядок задач не совпадает");
    }

    @Test
    public void checkCrossingTimeIntervalsMiddle() {
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 30);
        LocalDateTime startTime3 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 15);
        Task task1 = new Task(0, "Задача 1", "Описание з. 1", startTime1,30);
        Task task2 = new Task(0, "Задача 2", "Описание з. 2", startTime2,30);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        Epic epic = new Epic("Эпик1", "Описание э. 1");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask(0, epicId, "Подзадача", "Описание", startTime3, 30);
        taskManager.addSubtask(subtask);
        assertEquals(2, taskManager.getTasks().size(), "неверное число задач");
        assertEquals(0, taskManager.getSubtasks().size(), "неверное число подзадач");
        assertEquals(List.of(task1, task2), taskManager.getPrioritizedTasks(), "порядок задач не совпадает");
    }

    @Test
    public void checkTasksTiming() {
        Epic epic = new Epic("Эпик", "Описание");
        Task task = new Task("Задача", "Описание");
        int epicId = taskManager.addEpic(epic);
        Subtask subtask = new Subtask(epicId, "Подзадача", "Описание");
        assertNull(task.getStartTime());
        assertNull(task.getEndTime());
        assertNull(subtask.getStartTime());
        assertNull(subtask.getEndTime());

        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 30);
        Task task1 = new Task(0, "Задача 1", "Описание з. 1", startTime1,30);
        Subtask subtask1 = new Subtask(0, epicId, "Подзадача 1", "Описание 1", startTime2, 30);
        taskManager.addTask(task);
        taskManager.addSubtask(subtask);
        assertEquals(startTime1, task1.getStartTime());
        assertEquals(startTime2, subtask1.getStartTime());
        assertEquals(startTime1.plusMinutes(30), task1.getEndTime());
        assertEquals(startTime2.plusMinutes(30), subtask1.getEndTime());
    }

    @Test
    public void checkEpicsTiming() {
        Epic epic = new Epic("Эпик1", "Описание э. 1");
        int epicId = taskManager.addEpic(epic);
        assertNull(taskManager.getEpic(epicId).getStartTime());
        assertNull(taskManager.getEpic(epicId).getEndTime());
        LocalDateTime startTime1 = LocalDateTime.of(2010, Month.JUNE, 10, 8, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2010, Month.JUNE, 10, 10, 30);
        Subtask subtask1 = new Subtask(0, epicId, "Подзадача 1", "Описание", startTime1, 30);
        Subtask subtask2 = new Subtask(0, epicId, "Подзадача 2", "Описание", startTime2, 30);
        taskManager.addSubtask(subtask1);
        assertEquals(startTime1, epic.getStartTime());
        assertEquals(startTime1.plusMinutes(30), epic.getEndTime());
        taskManager.addSubtask(subtask2);
        assertEquals(startTime1, epic.getStartTime());
        assertEquals(startTime1.plusMinutes(180), epic.getEndTime());
    }

}