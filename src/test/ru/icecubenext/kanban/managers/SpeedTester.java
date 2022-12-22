package test.ru.icecubenext.kanban.managers;

import main.ru.icecubenext.kanban.managers.Manager;
import main.ru.icecubenext.kanban.managers.TaskManager;
import main.ru.icecubenext.kanban.model.Epic;
import main.ru.icecubenext.kanban.model.Subtask;
import main.ru.icecubenext.kanban.model.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SpeedTester {
    private int taskCount;
    private int epicCount;
    private int subtaskCount;

    public SpeedTester() {
        this.taskCount = 10_000;
        this.epicCount = 10_000;
        this.subtaskCount = 10_000;
    }

    public SpeedTester(int taskCount, int epicCount, int subtaskCount) {
        this.taskCount = taskCount;
        this.epicCount = epicCount;
        this.subtaskCount = subtaskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public void setEpicCount(int epicCount) {
        this.epicCount = epicCount;
    }

    public void setSubtaskCount(int subtaskCount) {
        this.subtaskCount = subtaskCount;
    }

    public void checkSpeedOfRealisation() {
        System.out.println("Начинаем тесты");
        System.out.println("        Тесты на добавление");
        System.out.println("Тест на добавление " + taskCount + " объектов Task");
        System.out.println("Завершен. Время: " + checkAddTask(taskCount) + "мс.");
        System.out.println("Тест на добавление " + epicCount + " объектов Epic");
        System.out.println("Завершен. Время: " + checkAddEpic(epicCount) + "мс.");
        System.out.println("Тест на добавление " + subtaskCount + " объектов Subtask");
        System.out.println("Завершен. Время: " + checkAddSubtask(subtaskCount) + "мс.");
        System.out.println("        Тесты на удаление");
        System.out.println("Тест на удаление " + taskCount + " (сразу всех) объектов Task");
        System.out.println("Завершен. Время: " + checkDeleteTasks(taskCount) + "мс.");
        System.out.println("Тест на удаление " + epicCount + " (сразу всех) объектов Epic");
        System.out.println("Завершен. Время: " + checkDeleteEpics(epicCount) + "мс.");
        System.out.println("Тест на удаление " + subtaskCount + " (сразу всех) объектов Subtask");
        System.out.println("Завершен. Время: " + checkDeleteSubtasks(subtaskCount) + "мс.");
        System.out.println("Тест на удаление " + taskCount + " (по отдельности) объектов Task");
        System.out.println("Завершен. Время: " + checkDeleteTask(taskCount) + "мс.");
        System.out.println("Тест на удаление " + epicCount + " (по отдельности) объектов Epic");
        System.out.println("Завершен. Время: " + checkDeleteEpic(epicCount) + "мс.");
        System.out.println("Тест на удаление " + subtaskCount + " (по отдельности) объектов Subtask");
        System.out.println("Завершен. Время: " + checkDeleteSubtask(subtaskCount) + "мс.");
        System.out.println("        Тесты на получение");
        System.out.println("Тест на получение " + taskCount + " (по отдельности) объектов Task");
        System.out.println("Завершен. Время: " + checkGetTask(taskCount) + "мс.");
        System.out.println("Тест на получение " + epicCount + " (по отдельности) объектов Epic");
        System.out.println("Завершен. Время: " + checkGetEpic(epicCount) + "мс.");
        System.out.println("Тест на получение " + subtaskCount + " (по отдельности) объектов Subtask");
        System.out.println("Завершен. Время: " + checkGetSubtask(subtaskCount) + "мс.");
        System.out.println("Тест на получение " + taskCount + " (сразу всех) объектов Task");
        System.out.println("Завершен. Время: " + checkGetTasks(taskCount) + "мс.");
        System.out.println("Тест на получение " + epicCount + " (сразу всех) объектов Epic");
        System.out.println("Завершен. Время: " + checkGetEpics(epicCount) + "мс.");
        System.out.println("Тест на получение " + subtaskCount + " (сразу всех) объектов Subtask");
        System.out.println("Завершен. Время: " + checkGetSubtasks(subtaskCount) + "мс.");
        System.out.println();

    }

    private long checkAddTask(int count) {
        TaskManager taskManager = Manager.getDefault();
        Instant start = Instant.now();
        fillTasks(taskManager, count);
        Instant finish = Instant.now();
        taskManager.deleteTasks();
        return Duration.between(start, finish).toMillis();
    }

    private long checkAddEpic(int count) {
        TaskManager taskManager = Manager.getDefault();
        Instant start = Instant.now();
        fillEpics(taskManager, count);
        Instant finish = Instant.now();
        taskManager.deleteEpics();
        return Duration.between(start, finish).toMillis();
    }

    private long checkAddSubtask(int count) {
        TaskManager taskManager = Manager.getDefault();
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epicId = taskManager.addEpic(epic1);
        Instant start = Instant.now();
        fillSubtasks(taskManager, count, epicId);
        Instant finish = Instant.now();
        taskManager.deleteSubtasks();
        return Duration.between(start, finish).toMillis();
    }

    private long checkDeleteTasks(int count){
        TaskManager taskManager = Manager.getDefault();
        fillTasks(taskManager, count);
        Instant start = Instant.now();
        taskManager.deleteTasks();
        Instant finish = Instant.now();
        return Duration.between(start, finish).toMillis();
    }

    private long checkDeleteEpics(int count) {
        TaskManager taskManager = Manager.getDefault();
        fillEpics(taskManager, count);
        Instant start = Instant.now();
        taskManager.deleteEpics();
        Instant finish = Instant.now();
        return Duration.between(start, finish).toMillis();
    }

    private long checkDeleteSubtasks(int count) {
        TaskManager taskManager = Manager.getDefault();
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epicId = taskManager.addEpic(epic1);
        fillSubtasks(taskManager, count, epicId);
        Instant start = Instant.now();
        taskManager.deleteSubtasks();
        Instant finish = Instant.now();
        taskManager.deleteEpics();
        return Duration.between(start, finish).toMillis();
    }

    private long checkDeleteTask(int count){
        TaskManager taskManager = Manager.getDefault();
        fillTasks(taskManager, count);
        System.out.print("Количество до: " + taskManager.getTasks().size());
        Instant start = Instant.now();
        for (int i = 0; i < count; i++) {
            taskManager.deleteTask(i);
        }
        Instant finish = Instant.now();
        System.out.println(" после: " + taskManager.getTasks().size());
        return Duration.between(start, finish).toMillis();
    }

    private long checkDeleteEpic(int count) {
        TaskManager taskManager = Manager.getDefault();
        fillEpics(taskManager, count);
        System.out.print("Количество до: " + taskManager.getEpics().size());
        Instant start = Instant.now();
        for (int i = 0; i < count; i++) {
            taskManager.deleteEpic(i);
        }
        Instant finish = Instant.now();
        System.out.println(" после: " + taskManager.getEpics().size());
        return Duration.between(start, finish).toMillis();
    }

    private long checkDeleteSubtask(int count) {
        TaskManager taskManager = Manager.getDefault();
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epicId = taskManager.addEpic(epic1);
        fillSubtasks(taskManager, count, epicId);
        System.out.print("Количество до: " + taskManager.getSubtasks().size());
        Instant start = Instant.now();
        for (int i = 1; i < count + 1; i++) {
            taskManager.deleteSubtask(i);
        }
        Instant finish = Instant.now();
        System.out.println(" после: " + taskManager.getSubtasks().size());
        taskManager.deleteEpics();
        return Duration.between(start, finish).toMillis();
    }

    private long checkGetTask(int count) {
        TaskManager taskManager = Manager.getDefault();
        fillTasks(taskManager, count);
        Task task;
        Instant start = Instant.now();
        for (int i = 0; i < count; i++) {
            task = taskManager.getTask(i);
        }
        Instant finish = Instant.now();
        taskManager.deleteTasks();
        return Duration.between(start, finish).toMillis();
    }

    private long checkGetEpic(int count) {
        TaskManager taskManager = Manager.getDefault();
        fillEpics(taskManager, count);
        Epic epic;
        Instant start = Instant.now();
        for (int i = 0; i < count; i++) {
            epic = taskManager.getEpic(i);
        }
        Instant finish = Instant.now();
        taskManager.deleteEpics();
        return Duration.between(start, finish).toMillis();
    }

    private long checkGetSubtask(int count) {
        TaskManager taskManager = Manager.getDefault();
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epicId = taskManager.addEpic(epic1);
        fillSubtasks(taskManager, count, epicId);
        Subtask subtask;
        Instant start = Instant.now();
        for (int i = 1; i < count + 1; i++) {
            subtask = taskManager.getSubtask(i);
        }
        Instant finish = Instant.now();
        taskManager.deleteSubtasks();
        return Duration.between(start, finish).toMillis();
    }

    private long checkGetTasks(int count) {
        TaskManager taskManager = Manager.getDefault();
        fillTasks(taskManager, count);
        List<Task> taskList = new ArrayList<>(count);
        Instant start = Instant.now();
        taskList.addAll(taskManager.getTasks());
        Instant finish = Instant.now();
        taskManager.deleteTasks();
        return Duration.between(start, finish).toMillis();
    }

    private long checkGetEpics(int count) {
        TaskManager taskManager = Manager.getDefault();
        fillEpics(taskManager, count);
        List<Epic> epicList = new ArrayList<>(count);
        Instant start = Instant.now();
        epicList.addAll(taskManager.getEpics());
        Instant finish = Instant.now();
        taskManager.deleteEpics();
        return Duration.between(start, finish).toMillis();
    }

    private long checkGetSubtasks(int count) {
        TaskManager taskManager = Manager.getDefault();
        Epic epic1 = new Epic("Эпик1", "Описание э. 1", null);
        int epicId = taskManager.addEpic(epic1);
        fillSubtasks(taskManager, count, epicId);
        List<Subtask> subtaskList = new ArrayList<>(count);
        Instant start = Instant.now();
        subtaskList.addAll(taskManager.getSubtasks());
        Instant finish = Instant.now();
        taskManager.deleteSubtasks();
        return Duration.between(start, finish).toMillis();
    }

    private void fillTasks(TaskManager taskManager, int count) {
        for (int i = 0; i < count; i++){
            taskManager.addTask(new Task("Задача " + i, "Описание з. 1"));
        }
    }

    private void fillEpics(TaskManager taskManager, int count) {
        for (int i = 0; i < count; i++){
            taskManager.addEpic(new Epic("Эпик " + i, "Описание э. 1.2", null));
        }
    }

    private void fillSubtasks(TaskManager taskManager, int count, int epicId){
        for (int i = 0; i < count; i++){
            taskManager.addSubtask(new Subtask(epicId, "Подзадача " + i, "Описание п. 1"));
        }
    }
}