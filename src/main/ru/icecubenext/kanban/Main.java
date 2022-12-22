package main.ru.icecubenext.kanban;

import main.ru.icecubenext.kanban.model.*;
import main.ru.icecubenext.kanban.managers.Manager;
import main.ru.icecubenext.kanban.managers.TaskManager;
import test.ru.icecubenext.kanban.managers.SpeedTester;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefault();
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        Epic epic2 = new Epic("Эпик1.2", "Описание э. 1.2", null);
        int epic1Id = taskManager.addEpic(epic1);
        int epic2Id = taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        int task1Id = taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);
        int subtask1Id = taskManager.addSubtask(subtask1);
        int subtask2Id = taskManager.addSubtask(subtask2);
        System.out.println(taskManager.getHistory());
        taskManager.getTask(task1Id);
        System.out.println(taskManager.getHistory());
        taskManager.getTask(task2Id);
        System.out.println(taskManager.getHistory());
        taskManager.getTask(task1Id);
        System.out.println(taskManager.getHistory());
        taskManager.deleteTasks();
        System.out.println(taskManager.getHistory());
        taskManager.getEpic(epic1Id);
        taskManager.getEpic(epic2Id);
        taskManager.getSubtask(subtask1Id);
        taskManager.getSubtask(subtask2Id);
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpic(epic1Id);
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpics();
        System.out.println(taskManager.getHistory());
        SpeedTester speedTester = new SpeedTester();
        speedTester.checkSpeedOfRealisation();
        System.out.println("Тесты в src/test");
    }
}

