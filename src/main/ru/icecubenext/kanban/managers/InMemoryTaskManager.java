package main.ru.icecubenext.kanban.managers;

import main.ru.icecubenext.kanban.model.Epic;
import main.ru.icecubenext.kanban.model.Subtask;
import main.ru.icecubenext.kanban.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int currentId;
    public HistoryManager historyManager = Manager.getDefaultHistory();
    private final HashMap<Integer, Task> tasksMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicsMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasksMap = new HashMap<>();
    private final HashMap<Integer, List<Subtask>> epicsSubtasksMap  = new HashMap<>();

    public InMemoryTaskManager() {
        this.currentId = 0;
    }

    public int addTask(Task task) {
        if (!tasksMap.containsValue(task)) {
            int id = getNewId();
            task.setId(id);
            tasksMap.put(id, task);
            return id;
        } else {
            System.out.println("Ошибка! Добавляемая задача уже существует");
            return -1;
        }
    }

    public int addEpic(Epic epic) {
        if (!epicsMap.containsValue(epic)) {
            int id = getNewId();
            epic.setId(id);
            List<Subtask> epicsSubtasks = epic.getSubtasks();
            epicsMap.put(id, epic);
            epicsSubtasksMap.put(id, epicsSubtasks);
            return id;
        } else {
            System.out.println("Ошибка! Добавляемый эпик уже существует");
            return -1;
        }

    }

    public int addSubtask(Subtask subtask) {
        if (subtasksMap.containsValue(subtask)) {
            System.out.println("Ошибка! Добавляемая подзадача уже существует");
            return -1;
        }
        int epicId = subtask.getEpicsId();
        if (epicsMap.containsKey(epicId)) {
            int id = getNewId();
            subtask.setId(id);
            subtasksMap.put(id, subtask);
            epicsSubtasksMap.get(epicId).add(subtask);
            return id;
        } else {
            System.out.println("Для добавляемой подзадачи не существует Эпика");
            return -1;
        }
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    public Task getTask(int id) {
        if (tasksMap.containsKey(id)) {
            historyManager.add(tasksMap.get(id));
            return tasksMap.get(id);
        } else {
            System.out.println("Ошибка! Не найдена задача с id=" + id);
            return null;
        }
    }

    public Epic getEpic(int id) {
        if (epicsMap.containsKey(id)) {
            historyManager.add(epicsMap.get(id));
            return epicsMap.get(id);
        } else {
            System.out.println("Ошибка! Не найден Эпик с id=" + id);
            return null;
        }
    }

    public Subtask getSubtask(int id) {
        if (subtasksMap.containsKey(id)) {
            historyManager.add(subtasksMap.get(id));
            return subtasksMap.get(id);
        } else {
            System.out.println("Ошибка! Не найдена подзадача с id=" + id);
            return null;
        }
    }

    public List<Subtask> getEpicsSubtasks(int id) {
        if (epicsSubtasksMap.containsKey(id)) {
            return epicsSubtasksMap.get(id);
        } else {
            System.out.println("Ошибка! Не найден Эпик с id=" + id);
            return new ArrayList<>();
        }
    }

    public boolean updateTask(Task task) {
        int id = task.getId();
        if (tasksMap.containsKey(id)) {
            tasksMap.put(id, task);
            return true;
        } else {
            System.out.println("Ошибка! Не найдена задача с id=" + id);
            return false;
        }
    }

    public boolean updateEpic(Epic epic) {
        int id = epic.getId();
        if (epicsMap.containsKey(id)) {
            List<Subtask> epicsSubtasks = epic.getSubtasks();
            epicsMap.put(id, epic);
            epicsSubtasksMap.put(id, epicsSubtasks);
            return true;
        } else {
            System.out.println("Ошибка! Не найден Эпик с id=" + id);
            return false;
        }
    }

    public boolean updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        if (subtasksMap.containsKey(id)) {
            subtasksMap.put(id, subtask);
            int epicId = subtask.getEpicsId();
            if (epicsSubtasksMap.containsKey(epicId)) {
                Epic epic = epicsMap.get(epicId);
                List<Subtask> epicsSubtasks = epic.getSubtasks();
                for (Subtask oldSubtask : epicsSubtasks) {
                    if (oldSubtask.getId() == id) {
                        epicsSubtasks.remove(oldSubtask);
                        epicsSubtasks.add(subtask);
                    }
                }
            }
            return true;
        } else {
            System.out.println("Ошибка! Не найдена подзадача с id=" + id);
            return false;
        }
    }

    public boolean deleteTasks() {
        tasksMap.clear();
        return true;
    }

    public boolean deleteEpics() {
        epicsMap.clear();
        epicsSubtasksMap.clear();
        subtasksMap.clear();
        return true;
    }

    public boolean deleteSubtasks() {
        subtasksMap.clear();
        for (Epic epic : epicsMap.values()) {
            epic.getSubtasks().clear();
        }
        return true;
    }

    public boolean deleteTask(int id) {
        if (tasksMap.containsKey(id)) {
            tasksMap.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteEpic(int id) {
        if (epicsMap.containsKey(id)) {
            Epic epic = epicsMap.get(id);
            for (Subtask subtask : epic.getSubtasks()) {
                subtasksMap.remove(subtask.getId());
            }
            epicsMap.remove(id);
            epicsSubtasksMap.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteSubtask(int id) {
        if (subtasksMap.containsKey(id)) {
            Subtask subtask = subtasksMap.get(id);
            int epicId = subtask.getEpicsId();
            if (epicsMap.containsKey(epicId)) {
                Epic epic = epicsMap.get(epicId);
                List<Subtask> epicsSubtasks = epic.getSubtasks();
                for (Subtask oldSubtask : epicsSubtasks) {
                    if (oldSubtask.getId() == id) {
                        epicsSubtasks.remove(oldSubtask);
                        break;
                    }
                }
            } else {
                System.out.println("При удалении подзадачи id=" + id + " возникла ошибка:");
                System.out.println("подзадача ссылалась на несуществующий Эпик id=" + epicId);
            }
            subtasksMap.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public int getNewId() {
        return this.currentId++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        InMemoryTaskManager taskManager = (InMemoryTaskManager) obj;
        return taskManager.currentId == this.currentId
                && Objects.equals(taskManager.tasksMap, this.tasksMap)
                && Objects.equals(taskManager.epicsMap, this.epicsMap)
                && Objects.equals(taskManager.subtasksMap, this.subtasksMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.currentId, this.tasksMap, this.epicsMap, this.subtasksMap);
    }

    @Override
    public String toString() {
        String result =  "ru.yandex.praktikum.kanban.TaskManager{" +
                "currentId='" + this.currentId;
        if (this.tasksMap != null) {
            result += ", tasksMap.size='" + this.tasksMap.size();
        } else {
            result += ", tasksMap.size=null";
        }

        if (this.epicsMap != null) {
            result += ", epicsMap.size='" + this.epicsMap.size();
        } else {
            result += ", epicsMap.size=null";
        }

        if (this.subtasksMap != null) {
            result += ", subtasksMap.size='" + this.subtasksMap.size();
        } else {
            result += ", subtasksMap.size=null";
        }
        return result + '}';
    }
}