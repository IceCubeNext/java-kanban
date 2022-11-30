package ru.icecubenext.kanban;

import ru.icecubenext.kanban.managers.*;
import ru.icecubenext.kanban.model.*;
import ru.icecubenext.kanban.model.Status;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        testFromPraktikum();
        addTest();
        getTest();
        updateTest();
        deleteTest();
    }
    
    private static void testFromPraktikum() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        int epic2Id = taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask(epic2Id, "Подзадача 3", "Описание п. 3");
        int subtask3Id = taskManager.addSubtask(subtask3);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println();

        Task task2update = new Task(task2Id, "Задача 2", "Описание з. 2");
        task2update.setStatus(Status.DONE);
        taskManager.updateTask(task2update);
        Subtask subtask3update = new Subtask(subtask3Id, epic2Id, "Подзадача 3", "Описание п. 3");
        Subtask subtask2update = new Subtask(subtask2Id, epic1Id, "Подзадача 2", "Описание п. 2");
        subtask2update.setStatus(Status.DONE);
        subtask3update.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2update);
        taskManager.updateSubtask(subtask3update);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println();
    }

    public static void addTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        Task task3 = new Task("Задача 3", "Описание з. 3");
        task2.setStatus(Status.IN_PROGRESS);
        task3.setStatus(Status.DONE);
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        int task3Id = taskManager.addTask(task3);
        boolean result = myAssertEquals(taskManager.getTask(task1Id).getStatus(), Status.NEW, "некорректный статус");
        result &= myAssertEquals(taskManager.getTask(task2Id).getStatus(), Status.IN_PROGRESS, "некорректный статус");
        result &= myAssertEquals(taskManager.getTask(task3Id).getStatus(), Status.DONE, "некорректный статус");

        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.NEW, "некорректный статус");
        result &= myAssertEquals(taskManager.getEpic(epic2Id).getStatus(), Status.NEW, "некорректный статус");

        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        taskManager.addSubtask(subtask1);
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.NEW, "некорректный статус");
        result &= myAssertEquals(taskManager.getEpic(epic2Id).getStatus(), Status.NEW, "некорректный статус");

        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.addSubtask(subtask2);
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.IN_PROGRESS, "некорректный статус");
        result &= myAssertEquals(taskManager.getEpic(epic2Id).getStatus(), Status.NEW, "некорректный статус");

        Subtask subtask3 = new Subtask(epic2Id, "Подзадача 3", "Описание п. 3");
        subtask3.setStatus(Status.DONE);
        taskManager.addSubtask(subtask3);
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.IN_PROGRESS, "некорректный статус");
        result &= myAssertEquals(taskManager.getEpic(epic2Id).getStatus(), Status.DONE, "некорректный статус");

        Epic epic3 = new Epic("Эпик3", "Описание э. 3", null);
        epic3.setStatus(Status.DONE);
        int epic3Id = taskManager.addEpic(epic3);
        result &= myAssertEquals(taskManager.getEpic(epic3Id).getStatus(), Status.NEW, "некорректный статус");
        result &= myAssertInt(epic1.getSubtasks().size(), 2, "некорректное количество подзадач");
        result &= myAssertInt(epic2.getSubtasks().size(), 1, "некорректное количество подзадач");
        result &= myAssertEquals(epic3.getSubtasks().size(), 0, "некорректное количество подзадач");

        if (result) {
            System.out.println("Тесты на добавление объектов пройдены");
        }
    }

    public static void getTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        boolean result = myAssertEquals(taskManager.getTask(task1Id), task1, "получен не тот объект");
        result &= myAssertEquals(taskManager.getTask(task2Id), task2, "получен не тот объект");
        int taskCount = taskManager.getTasks().size();
        result &= myAssertInt(taskCount, 2, "неверное количество объектов");

        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        result &= myAssertEquals(taskManager.getEpic(epic1Id), epic1, "получен не тот объект");
        result &= myAssertEquals(taskManager.getEpic(epic2Id), epic2, "получен не тот объект");
        int epicCount = taskManager.getEpics().size();
        result &= myAssertInt(epicCount, 2, "неверное количество объектов");

        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        Subtask subtask3 = new Subtask(epic2Id, "Подзадача 3", "Описание п. 3");
        int subtask1Id = taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);
        result &= myAssertEquals(taskManager.getSubtask(subtask1Id), subtask1, "получен не тот объект");
        result &= myAssertEquals(taskManager.getSubtask(subtask3Id), subtask3, "получен не тот объект");
        int subtaskCount = taskManager.getSubtasks().size();
        result &= myAssertInt(subtaskCount, 3, "неверное количество объектов");
        int epics1SubtasksCount = taskManager.getEpicsSubtasks(epic1Id).size();
        int epics2SubtasksCount = taskManager.getEpicsSubtasks(epic2Id).size();
        result &= myAssertInt(epics1SubtasksCount, 2, "неверное количество объектов");
        result &= myAssertInt(epics2SubtasksCount, 1, "неверное количество объектов");

        if (result) {
            System.out.println("Тесты на получение объектов пройдены");
        }
    }

    public static void updateTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача 1", "Описание з. 1");
        int task1Id = taskManager.addTask(task1);
        Task updatableTask = new Task(task1Id, "Задача 2", "Описание з. 2");
        updatableTask.setStatus(Status.DONE);
        taskManager.updateTask(updatableTask);
        boolean result = myAssertInt(taskManager.getTasks().size(), 1, "неверное количество объектов");
        result &= myAssertEquals(taskManager.getTask(task1Id).getName(), "Задача 2", "поля не совпадают");
        result &= myAssertEquals(taskManager.getTask(task1Id).getDescription(), "Описание з. 2", "поля не совпадают");
        result &= myAssertEquals(taskManager.getTask(task1Id).getStatus(), Status.DONE, "поля не совпадают");

        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        result &= myAssertInt(taskManager.getEpics().size(), 2, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(),0, "неверное количество объектов");

        Epic updatableEpic = new Epic(epic1Id, "Обновленный эпик 1", "Обновленное описание э. 1", null);
        taskManager.updateEpic(updatableEpic);
        result &= myAssertInt(taskManager.getEpics().size(), 2, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(),0, "неверное количество объектов");
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getName(), "Обновленный эпик 1", "поля не совпадают");
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getDescription(), "Обновленное описание э. 1", "поля не совпадают");

        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(taskManager.getSubtask(subtask1Id));
        subtasks.add(taskManager.getSubtask(subtask2Id));
        updatableEpic = new Epic(epic1Id, "Обновленный эпик 1", "Обновленное описание э. 1", subtasks);
        taskManager.updateEpic(updatableEpic);
        result &= myAssertInt(taskManager.getEpics().size(), 2, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(),2, "неверное количество объектов");
        result &= myAssertInt(taskManager.getEpic(epic1Id).getSubtasks().size(), 2, "неверное количество объектов");
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getSubtasks(), subtasks, "поля не совпадают");
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.NEW, "поля не совпадают");

        Subtask subtask3 = new Subtask(epic1Id, "Подзадача 3", "Описание п. 3");
        int subtask3Id = taskManager.addSubtask(subtask3);
        result &= myAssertInt(taskManager.getEpics().size(), 2, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(),3, "неверное количество объектов");
        result &= myAssertInt(taskManager.getEpic(epic1Id).getSubtasks().size(), 3, "неверное количество объектов");
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.NEW, "поля не совпадают");
        result &= myAssertEquals(taskManager.getSubtask(subtask3Id).getStatus(), Status.NEW, "поля не совпадают");

        Subtask updatableSubtask = new Subtask(subtask3Id, epic1Id, "Обновленная подзадача 3", "Обновленное описание п. 3");
        updatableSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(updatableSubtask);
        result &= myAssertInt(taskManager.getEpics().size(), 2, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(),3, "неверное количество объектов");
        result &= myAssertInt(taskManager.getEpic(epic1Id).getSubtasks().size(), 3, "неверное количество объектов");
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.IN_PROGRESS, "поля не совпадают");
        result &= myAssertEquals(taskManager.getSubtask(subtask3Id).getStatus(), Status.DONE, "поля не совпадают");

        Subtask subtask4 = new Subtask(epic2Id, "Подзадача 4", "Описание п. 4");
        int subtask4Id = taskManager.addSubtask(subtask4);
        result &= myAssertInt(taskManager.getEpics().size(), 2, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(),4, "неверное количество объектов");
        result &= myAssertInt(taskManager.getEpic(epic1Id).getSubtasks().size(), 3, "неверное количество объектов");
        result &= myAssertInt(taskManager.getEpic(epic2Id).getSubtasks().size(), 1, "неверное количество объектов");
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.IN_PROGRESS, "поля не совпадают");
        result &= myAssertEquals(taskManager.getSubtask(subtask4Id).getStatus(), Status.NEW, "поля не совпадают");
        result &= myAssertEquals(taskManager.getEpic(epic2Id).getStatus(), Status.NEW, "поля не совпадают");

        updatableSubtask = new Subtask(subtask4Id, epic2Id, "Обновленная подзадача 4", "Обновленное описание п. 4");
        updatableSubtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(updatableSubtask);
        result &= myAssertEquals(taskManager.getEpic(epic2Id).getStatus(), Status.IN_PROGRESS, "поля не совпадают");
        result &= myAssertEquals(taskManager.getSubtask(subtask4Id).getStatus(), Status.IN_PROGRESS, "поля не совпадают");

        updatableSubtask = new Subtask(subtask4Id, epic2Id, "Обновленная подзадача 4", "Обновленное описание п. 4");
        updatableSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(updatableSubtask);
        result &= myAssertEquals(taskManager.getEpic(epic2Id).getStatus(), Status.DONE, "поля не совпадают");
        result &= myAssertEquals(taskManager.getSubtask(subtask4Id).getStatus(), Status.DONE, "поля не совпадают");

        taskManager.deleteSubtasks();
        taskManager.deleteEpics();
        taskManager.deleteTasks();
        result &= myAssertInt(taskManager.getEpics().size(), 0, "неверное количество объектов");
        result &= myAssertInt(taskManager.getTasks().size(), 0, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(),0, "неверное количество объектов");

        if (result) {
            System.out.println("Тесты на обновление объектов пройдены");
        }
    }

    public static void deleteTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        task2.setStatus(Status.IN_PROGRESS);
        Task task3 = new Task("Задача 3", "Описание з. 3");
        task3.setStatus(Status.DONE);
        int task1Id = taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        boolean result = myAssertInt(taskManager.getTasks().size(), 3, "неверное количество объектов");
        taskManager.deleteTask(task1Id);
        result &= myAssertInt(taskManager.getTasks().size(), 2, "неверное количество объектов");
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task2);
        tasks.add(task3);
        result &= myAssertEquals(taskManager.getTasks(), tasks, "поля не совпадают");
        taskManager.deleteTasks();
        result &= myAssertInt(taskManager.getTasks().size(), 0, "поля не совпадают");

        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик2", "Описание э. 2", null);
        Epic epic3 = new Epic("Эпик2", "Описание э. 2", null);
        taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        result &= myAssertInt(taskManager.getEpics().size(), 3, "неверное количество объектов");
        taskManager.deleteEpic(epic2Id);
        result &= myAssertInt(taskManager.getEpics().size(), 2, "неверное количество объектов");
        ArrayList<Epic> epics = new ArrayList<>();
        epics.add(epic1);
        epics.add(epic3);
        result &= myAssertEquals(taskManager.getEpics(), epics, "поля не совпадают");
        taskManager.deleteEpics();
        result &= myAssertInt(taskManager.getEpics().size(), 0, "поля не совпадают");

        int epic1Id = taskManager.addEpic(epic1);
        epic2Id = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        subtask2.setStatus(Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask(epic2Id, "Подзадача 3", "Описание п. 3");
        subtask3.setStatus(Status.DONE);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        int subtask3Id = taskManager.addSubtask(subtask3);
        result &= myAssertInt(taskManager.getEpic(epic1Id).getSubtasks().size(), 2, "неверное количество объектов");
        result &= myAssertInt(taskManager.getEpic(epic2Id).getSubtasks().size(), 1, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(), 3, "неверное количество объектов");
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.IN_PROGRESS, "поля не совпадают");
        result &= myAssertEquals(taskManager.getEpic(epic2Id).getStatus(), Status.DONE, "поля не совпадают");

        taskManager.deleteSubtask(subtask3Id);
        result &= myAssertInt(taskManager.getEpic(epic1Id).getSubtasks().size(), 2, "неверное количество объектов");
        result &= myAssertInt(taskManager.getEpic(epic2Id).getSubtasks().size(), 0, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(), 2, "неверное количество объектов");
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.IN_PROGRESS, "поля не совпадают");
        result &= myAssertEquals(taskManager.getEpic(epic2Id).getStatus(), Status.NEW, "поля не совпадают");

        taskManager.deleteSubtasks();
        result &= myAssertInt(taskManager.getEpic(epic1Id).getSubtasks().size(), 0, "неверное количество объектов");
        result &= myAssertInt(taskManager.getEpic(epic2Id).getSubtasks().size(), 0, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(), 0, "неверное количество объектов");
        result &= myAssertEquals(taskManager.getEpic(epic1Id).getStatus(), Status.NEW, "поля не совпадают");
        result &= myAssertEquals(taskManager.getEpic(epic2Id).getStatus(), Status.NEW, "поля не совпадают");

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        result &= myAssertInt(taskManager.getEpic(epic1Id).getSubtasks().size(), 2, "неверное количество объектов");
        result &= myAssertInt(taskManager.getEpic(epic2Id).getSubtasks().size(), 1, "неверное количество объектов");
        taskManager.deleteEpic(epic1Id);
        result &= myAssertInt(taskManager.getEpics().size(), 1, "неверное количество объектов");
        result &= myAssertInt(taskManager.getEpic(epic2Id).getSubtasks().size(), 1, "неверное количество объектов");
        result &= myAssertInt(taskManager.getSubtasks().size(), 1, "неверное количество объектов");

        if (result) {
            System.out.println("Тесты на удаление объектов пройдены");
        }
    }

    private static boolean myAssertInt(int a, int b, String message) {
        if(a != b) {
            throw new AssertionError(a + " != " + b + "; " + message);
        }
        return true;
    }

    private static boolean myAssertEquals(Object a, Object b, String message) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        if(!a.equals(b)) {
            throw new AssertionError(a + " != " + b + "; " + message);
        }
        return true;
    }
}