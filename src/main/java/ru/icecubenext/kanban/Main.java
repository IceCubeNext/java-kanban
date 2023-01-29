package ru.icecubenext.kanban;

import ru.icecubenext.kanban.managers.Manager;
import ru.icecubenext.kanban.managers.exceptions.ManagerSaveException;
import ru.icecubenext.kanban.managers.impl.FileBackedTaskManager;
import ru.icecubenext.kanban.model.*;
import ru.icecubenext.kanban.managers.TaskManager;
import java.io.File;


public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        //TaskManager taskManager = FileBackedTaskManager.loadFromFile(new File("C:\\Users\\Александр\\kanban.csv"));
        TaskManager taskManager = Manager.getDefault();
        Task task = new Task("name", "description");
        int taskId = taskManager.addTask(task);
        taskManager.getTask(taskId).setDuration(1);
        System.out.println(taskManager.getTask(taskId).getEndTime());
        Epic epic = new Epic("ename", "edescription", null);
        int epic1Id = taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(epic1Id, "sname", "sdescription");
        Subtask subtask2 = new Subtask(epic1Id, "sname", "sdescription2");
        System.out.println(taskManager.getEpic(epic1Id).getEndTime());
        int subtask1id = taskManager.addSubtask(subtask1);
        System.out.println(taskManager.getEpic(epic1Id).getEndTime());
        int subtask2id = taskManager.addSubtask(subtask2);
        System.out.println(taskManager.getEpic(epic1Id).getEndTime());
        System.out.println(taskManager.getPrioritizedTasks());
    }
}

