package ru.icecubenext.kanban;

import ru.icecubenext.kanban.managers.exceptions.ManagerSaveException;
import ru.icecubenext.kanban.managers.impl.FileBackedTasksManager;
import ru.icecubenext.kanban.model.*;
import ru.icecubenext.kanban.managers.TaskManager;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        TaskManager taskManager = new FileBackedTasksManager();
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик1.2", "Описание э. 1.2", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic2Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic2Id, "Подзадача 2", "Описание п. 2");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        taskManager.getEpic(epic1Id);
        taskManager.getTask(task1Id);
        taskManager.getSubtask(subtask1Id);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getHistory());

        String HOME = System.getProperty("user.home");
        TaskManager taskManager2 = new FileBackedTasksManager(HOME + "\\kanban.csv");
        System.out.println(taskManager2.getTasks());
        System.out.println(taskManager2.getEpics());
        System.out.println(taskManager2.getSubtasks());
        System.out.println(taskManager2.getHistory());
        System.out.println("next id = " + taskManager2.addTask(new Task("test", "test")));
    }
}

