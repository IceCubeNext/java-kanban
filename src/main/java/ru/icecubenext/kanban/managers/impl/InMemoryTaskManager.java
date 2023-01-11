package ru.icecubenext.kanban.managers.impl;

import lombok.extern.log4j.Log4j;
import ru.icecubenext.kanban.managers.HistoryManager;
import ru.icecubenext.kanban.managers.Manager;
import ru.icecubenext.kanban.managers.TaskManager;
import ru.icecubenext.kanban.model.Epic;
import ru.icecubenext.kanban.model.Subtask;
import ru.icecubenext.kanban.model.Task;

import java.util.*;
@Log4j
public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager historyManager = Manager.getDefaultHistory();
    private int currentId;
    private final HashMap<Integer, Task> tasksMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicsMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasksMap = new HashMap<>();
    private final HashMap<Integer, List<Subtask>> epicsSubtasksMap  = new HashMap<>();

    public InMemoryTaskManager() {
        this.currentId = 0;
    }

    public int addTask(Task task) {
        if (!tasksMap.containsValue(task)) {
            if (task.getId() == 0) {
                int id = getNewId();
                task.setId(id);
            }
            tasksMap.put(task.getId(), task);
            return task.getId();
        } else {
            log.debug("Ошибка! Добавляемая задача уже существует");
            return -1;
        }
    }

    public int addEpic(Epic epic) {
        if (!epicsMap.containsValue(epic)) {
            if (epic.getId() == 0) {
                int id = getNewId();
                epic.setId(id);
            }
            List<Subtask> epicsSubtasks = epic.getSubtasks();
            epicsMap.put(epic.getId(), epic);
            epicsSubtasksMap.put(epic.getId(), epicsSubtasks);
            return epic.getId();
        } else {
            log.debug("Ошибка! Добавляемый эпик уже существует");
            return -1;
        }

    }

    public int addSubtask(Subtask subtask) {
        if (subtasksMap.containsValue(subtask)) {
            log.debug("Ошибка! Добавляемая подзадача уже существует");
            return -1;
        }
        int epicId = subtask.getEpicsId();
        if (epicsMap.containsKey(epicId)) {
            if (subtask.getId() == 0) {
                int id = getNewId();
                subtask.setId(id);
            }
            subtasksMap.put(subtask.getId(), subtask);
            epicsSubtasksMap.get(epicId).add(subtask);
            return subtask.getId();
        } else {
            log.debug("Для добавляемой подзадачи не существует Эпика");
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
            addToHistory(tasksMap.get(id));
            return tasksMap.get(id);
        } else {
            log.debug("Ошибка! Не найдена задача с id=" + id);
            return null;
        }
    }

    public Epic getEpic(int id) {
        if (epicsMap.containsKey(id)) {
            addToHistory(epicsMap.get(id));
            return epicsMap.get(id);
        } else {
            log.debug("Ошибка! Не найден Эпик с id=" + id);
            return null;
        }
    }

    public Subtask getSubtask(int id) {
        if (subtasksMap.containsKey(id)) {
            addToHistory(subtasksMap.get(id));
            return subtasksMap.get(id);
        } else {
            log.debug("Ошибка! Не найдена подзадача с id=" + id);
            return null;
        }
    }

    public List<Subtask> getEpicsSubtasks(int id) {
        if (epicsSubtasksMap.containsKey(id)) {
            return epicsSubtasksMap.get(id);
        } else {
            log.debug("Ошибка! Не найден Эпик с id=" + id);
            return new ArrayList<>();
        }
    }

    public boolean updateTask(Task task) {
        int id = task.getId();
        if (tasksMap.containsKey(id)) {
            tasksMap.put(id, task);
            return true;
        } else {
            log.debug("Ошибка! Не найдена задача с id=" + id);
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
            log.debug("Ошибка! Не найден Эпик с id=" + id);
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
            log.debug("Ошибка! Не найдена подзадача с id=" + id);
            return false;
        }
    }

    public boolean deleteTasks() {
        for (int id : tasksMap.keySet()) {
            removeFromHistory(id);
        }
        tasksMap.clear();
        return true;
    }

    public boolean deleteEpics() {
        for (int id : epicsMap.keySet()) {
            removeFromHistory(id);
        }
        epicsMap.clear();
        epicsSubtasksMap.clear();
        for (int id : subtasksMap.keySet()) {
            removeFromHistory(id);
        }
        subtasksMap.clear();
        return true;
    }

    public boolean deleteSubtasks() {
        for (int id : subtasksMap.keySet()) {
            removeFromHistory(id);
        }
        subtasksMap.clear();
        for (Epic epic : epicsMap.values()) {
            epic.getSubtasks().clear();
        }
        return true;
    }

    public boolean deleteTask(int id) {
        if (tasksMap.containsKey(id)) {
            tasksMap.remove(id);
            removeFromHistory(id);
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
                removeFromHistory(subtask.getId());
            }
            epicsMap.remove(id);
            epicsSubtasksMap.remove(id);
            removeFromHistory(id);
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
                log.debug("При удалении подзадачи id=" + id + " возникла ошибка:");
                log.debug("подзадача ссылалась на несуществующий Эпик id=" + epicId);
            }
            subtasksMap.remove(id);
            removeFromHistory(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
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
        String result =  "TaskManager{" + "currentId='" + this.currentId;
        result += ", tasksMap.size='" + this.tasksMap.size();
        result += ", epicsMap.size='" + this.epicsMap.size();
        result += ", subtasksMap.size='" + this.subtasksMap.size();
        return result + '}';
    }

    protected void addToHistory(Task task) {
        historyManager.add(task);
    }

    protected void removeFromHistory(int id) {
        historyManager.remove(id);
    }

    protected void setCurrentId(int id) {
        this.currentId = id;
    }

    private int getNewId() {
        return this.currentId++;
    }
}
