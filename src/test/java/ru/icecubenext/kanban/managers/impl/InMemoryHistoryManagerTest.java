//package ru.icecubenext.kanban.managers.impl;
//
//public class InMemoryHistoryManagerTest {
//
//    @Test
//    public void add() {
//        TaskManager taskManager = Manager.getDefault();
//        Assert.assertEquals(0, taskManager.getTasks().size());
//        Task task1 = new Task("Задача 1", "Описание з. 1");
//        Epic epic1 = new Epic("Эпик 1", "Описание э. 1", null);
//        int epic1Id = taskManager.addEpic(epic1);
//        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
//        int task1Id = taskManager.addTask(task1);
//        int subtask1Id = taskManager.addSubtask(subtask1);
//        List<Task> expected = new ArrayList<>();
//
//        Assert.assertEquals(0, taskManager.getHistory().size());
//        taskManager.getTask(task1Id);
//        expected.add(task1);
//        Assert.assertEquals(1, taskManager.getHistory().size());
//        taskManager.getTask(task1Id);
//        Assert.assertEquals(1, taskManager.getHistory().size());
//        taskManager.getEpic(epic1Id);
//        expected.add(epic1);
//        Assert.assertEquals(2, taskManager.getHistory().size());
//        taskManager.getEpic(epic1Id);
//        Assert.assertEquals(2, taskManager.getHistory().size());
//        taskManager.getSubtask(subtask1Id);
//        expected.add(subtask1);
//        Assert.assertEquals(3, taskManager.getHistory().size());
//        taskManager.getSubtask(subtask1Id);
//        Assert.assertEquals(3, taskManager.getHistory().size());
//        Assert.assertEquals(expected, taskManager.getHistory());
//    }
//
//    @Test
//    public void getHistory() {
//        TaskManager taskManager = Manager.getDefault();
//        for (int i = 0; i < 11; i++) {
//            Task task = new Task("Задача " + i, "Описание з. 1");
//            int taskId = taskManager.addTask(task);
//            taskManager.getTask(taskId);
//        }
//        Assert.assertEquals(11, taskManager.getHistory().size());
//        taskManager.deleteTasks();
//        Assert.assertEquals(0, taskManager.getHistory().size());
//
//        Task task = new Task("Задача ", "Описание з. 1");
//        int taskId = taskManager.addTask(task);
//        taskManager.getTask(taskId);
//        Assert.assertEquals(1, taskManager.getHistory().size());
//        taskManager.deleteTask(taskId);
//        Assert.assertEquals(0, taskManager.getHistory().size());
//
//        for (int i = 0; i < 3; i++) {
//            Epic epic = new Epic("Эпик 1", "Описание э. 1", null);
//            int epic1Id = taskManager.addEpic(epic);
//            taskManager.getEpic(epic1Id);
//        }
//        Assert.assertEquals(3, taskManager.getHistory().size());
//        taskManager.deleteEpics();
//        Assert.assertEquals(0, taskManager.getHistory().size());
//
//        Epic epic1 = new Epic("Эпик 1", "Описание э. 1", null);
//        int epic1Id = taskManager.addEpic(epic1);
//        taskManager.getEpic(epic1Id);
//        Assert.assertEquals(1, taskManager.getHistory().size());
//        taskManager.deleteEpic(epic1Id);
//        Assert.assertEquals(0, taskManager.getHistory().size());
//
//        epic1 = new Epic("Эпик 1", "Описание э. 1", null);
//        epic1Id = taskManager.addEpic(epic1);
//        for (int i = 0; i < 3; i++) {
//            Subtask subtask = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
//            int subtask1Id = taskManager.addSubtask(subtask);
//            taskManager.getSubtask(subtask1Id);
//        }
//        Assert.assertEquals(3, taskManager.getHistory().size());
//        taskManager.deleteSubtasks();
//        Assert.assertEquals(0, taskManager.getHistory().size());
//
//        epic1 = new Epic("Эпик 1", "Описание э. 1", null);
//        epic1Id = taskManager.addEpic(epic1);
//        Subtask subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
//        int subtask1Id = taskManager.addSubtask(subtask1);
//        taskManager.getSubtask(subtask1Id);
//        Assert.assertEquals(1, taskManager.getHistory().size());
//        taskManager.deleteSubtask(subtask1Id);
//        Assert.assertEquals(0, taskManager.getHistory().size());
//
//        epic1 = new Epic("Эпик 1", "Описание э. 1", null);
//        epic1Id = taskManager.addEpic(epic1);
//        subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
//        subtask1Id = taskManager.addSubtask(subtask1);
//        Subtask subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
//        int subtask2Id = taskManager.addSubtask(subtask2);
//        taskManager.getEpic(epic1Id);
//        taskManager.getSubtask(subtask1Id);
//        taskManager.getSubtask(subtask2Id);
//        List<Task> expected = new ArrayList<>(List.of(taskManager.getEpic(epic1Id),
//                taskManager.getSubtask(subtask1Id),
//                taskManager.getSubtask(subtask2Id)));
//        Assert.assertEquals(3, taskManager.getHistory().size());
//        Assert.assertEquals(expected, taskManager.getHistory());
//        taskManager.deleteSubtask(subtask2Id);
//        expected.remove(expected.size() - 1);
//        Assert.assertEquals(2, taskManager.getHistory().size());
//        Assert.assertEquals(expected, taskManager.getHistory());
//        taskManager.deleteEpic(epic1Id);
//        expected.clear();
//        Assert.assertEquals(0, taskManager.getHistory().size());
//        Assert.assertEquals(expected, taskManager.getHistory());
//
//        epic1 = new Epic("Эпик 1", "Описание э. 1", null);
//        epic1Id = taskManager.addEpic(epic1);
//        subtask1 = new Subtask(epic1Id, "Подзадача 1", "Описание п. 1");
//        subtask1Id = taskManager.addSubtask(subtask1);
//        subtask2 = new Subtask(epic1Id, "Подзадача 2", "Описание п. 2");
//        subtask2Id = taskManager.addSubtask(subtask2);
//        taskManager.getEpic(epic1Id);
//        taskManager.getSubtask(subtask1Id);
//        taskManager.getSubtask(subtask2Id);
//        Assert.assertEquals(3, taskManager.getHistory().size());
//        taskManager.deleteSubtasks();
//        Assert.assertEquals(1, taskManager.getHistory().size());
//        taskManager.deleteEpic(epic1Id);
//        Assert.assertEquals(0, taskManager.getHistory().size());
//    }
//}