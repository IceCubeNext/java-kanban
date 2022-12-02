package main.ru.icecubenext.kanban;

import main.ru.icecubenext.kanban.model.*;
import main.ru.icecubenext.kanban.managers.Manager;
import main.ru.icecubenext.kanban.managers.TaskManager;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        TaskManager taskManager = manager.getDefault();
        Task task1 = new Task("Задача 1", "Описание з. 1");
        Task task2 = new Task("Задача 2", "Описание з. 2");
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epic1Id = taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println("Тесты в src/test");
    }
}