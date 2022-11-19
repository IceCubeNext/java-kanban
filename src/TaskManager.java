import java.util.*;

public class TaskManager implements ITaskManager {
    private int currentId;
    private final HashMap<Integer, Task> tasksMap;
    private final HashMap<Integer, Epic> epicsMap;
    private final HashMap<Integer, Subtask> subtasksMap;

    TaskManager() {
        this.currentId = 0;
        tasksMap = new HashMap<>();
        epicsMap = new HashMap<>();
        subtasksMap = new HashMap<>();
    }

    TaskManager(int currentId,
                HashMap<Integer, Task> tasksMap,
                HashMap<Integer, Epic> epicsMap,
                HashMap<Integer, Subtask> subtasksMap) {
        this.currentId = currentId;
        this.tasksMap = tasksMap;
        this.epicsMap = epicsMap;
        this.subtasksMap = subtasksMap;
    }

    public int addTask(Task task) {
        int id = getNewId();
        task.setId(id);
        tasksMap.put(id, task);
        return id;
    }

    public int addEpic(Epic epic) {
        int id = getNewId();
        epic.setId(id);
        epicsMap.put(id, epic);
        return id;
    }

    public int addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicsId();
        if (epicsMap.containsKey(epicId)) {
            int id = getNewId();
            subtask.setId(id);
            subtasksMap.put(id, subtask);
            Epic currentEpic = epicsMap.get(epicId);
            currentEpic.addSubtask(subtask);
            return id;
        } else {
            System.out.println("Для добавляемой подзадачи не существует Эпика");
            return -1;
        }
    }

    public ArrayList<Task> getTasks() {
        if (tasksMap == null) return null;
        ArrayList<Task> result = new ArrayList<>();
        for (Integer id: getSortedKeysOfTaskMap(tasksMap)) {
            result.add(tasksMap.get(id));
        }
        return result;
    }

    public ArrayList<Epic> getEpics() {
        if (epicsMap == null) return null;
        ArrayList<Epic> result = new ArrayList<>();
        for (Integer id: getSortedKeysOfTaskMap(epicsMap)) {
            result.add(epicsMap.get(id));
        }
        return result;
    }

    public ArrayList<Subtask> getSubtasks() {
        if (subtasksMap == null) return null;
        ArrayList<Subtask> result = new ArrayList<>();
        for (Integer id: getSortedKeysOfTaskMap(subtasksMap)) {
            result.add(subtasksMap.get(id));
        }
        return result;
    }

    public Task getTask(int id) {
        if (tasksMap == null) return null;
        if (tasksMap.containsKey(id)) {
            return tasksMap.get(id);
        } else {
            System.out.println("Ошибка! Не найдена задача с id=" + id);
            return null;
        }
    }

    public Epic getEpic(int id) {
        if (epicsMap == null) return null;
        if (epicsMap.containsKey(id)) {
            return epicsMap.get(id);
        } else {
            System.out.println("Ошибка! Не найден Эпик с id=" + id);
            return null;
        }
    }

    public Subtask getSubtask(int id) {
        if (subtasksMap == null) return null;
        if (subtasksMap.containsKey(id)) {
            return subtasksMap.get(id);
        } else {
            System.out.println("Ошибка! Не найдена подзадача с id=" + id);
            return null;
        }
    }

    public ArrayList<Subtask> getEpicsSubtasks(int id) {
        if (epicsMap == null) return null;
        if (epicsMap.containsKey(id)) {
            Epic epic = epicsMap.get(id);
            return epic.getSubtasks();
        } else {
            System.out.println("Ошибка! Не найден Эпик с id=" + id);
            return null;
        }
    }

    public boolean updateTask(Task task) {
        int id = task.getId();
        if (tasksMap.containsKey(id)) {
            Task currentTask = tasksMap.get(id);
            currentTask.setName(task.getName());
            currentTask.setDescription(task.getDescription());
            currentTask.setStatus(task.getStatus());
            return true;
        } else {
            System.out.println("Ошибка! Не найдена задача с id=" + id);
            return false;
        }
    }

    public boolean updateEpic(Epic epic) {
        int id = epic.getId();
        if (epicsMap.containsKey(id)) {
            Epic currentEpic = epicsMap.get(id);
            currentEpic.setName(epic.getName());
            currentEpic.setDescription(epic.getDescription());
            currentEpic.setStatus(epic.getStatus());
            updateEpicsSubtasks(currentEpic, epic);
//            if (epic.getSubtasks() == null && currentEpic.getSubtasks() == null) {
//                return true;
//            } else if (epic.getSubtasks() == null || currentEpic.getSubtasks() == null) {
//                recalculateSubtasks(currentEpic.getSubtasks(), epic.getSubtasks());
//            } else if (!epic.getSubtasks().equals(currentEpic.getSubtasks())) {
//                recalculateSubtasks(currentEpic.getSubtasks(), epic.getSubtasks());
//            }
            return true;
        } else {
            System.out.println("Ошибка! Не найден Эпик с id=" + id);
            return false;
        }
    }

    public boolean updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        if (subtasksMap.containsKey(id) && epicsMap.containsKey(subtask.getEpicsId())) {
            Subtask currentSubtask = subtasksMap.get(id);
            if (currentSubtask.getEpicsId() != subtask.getEpicsId()) {
                Epic oldEpic = epicsMap.get(currentSubtask.getEpicsId());
                oldEpic.deleteSubtask(id);
                Epic newEpic = epicsMap.get(subtask.getEpicsId());
                newEpic.addSubtask(subtask);
            } else {
                Epic oldEpic = epicsMap.get(subtask.getEpicsId());
                oldEpic.updateSubtask(subtask);
            }
            currentSubtask.setEpicsId(subtask.getEpicsId());
            currentSubtask.setName(subtask.getName());
            currentSubtask.setDescription(subtask.getDescription());
            currentSubtask.setStatus(subtask.getStatus());
            return true;
        } else {
            System.out.println("Ошибка при обновлении подзадачи!");
            System.out.println("Не найдена подзадача с id=" + id + " или эпик с id=" + subtask.getEpicsId());
            return false;
        }
    }

    public boolean deleteTasks() {
        boolean result = true;
        ArrayList<Integer> keys = new ArrayList<>(tasksMap.keySet());
        for (Integer id : keys) {
            result = result & deleteTask(id);
        }
        return result;
    }

    public boolean deleteEpics() {
        boolean result = true;
        ArrayList<Integer> keys = new ArrayList<>(epicsMap.keySet());
        for (Integer id : keys) {
            result = result & deleteEpic(id);
        }
        return result;
    }

    public boolean deleteSubtasks() {
        boolean result = true;
        ArrayList<Integer> keys = new ArrayList<>(subtasksMap.keySet());
        for (Integer id : keys) {
            result = result & deleteSubtask(id);
        }
        return result;
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
            Epic currentEpic = epicsMap.get(id);
            if (currentEpic.getSubtasks() != null) {
                for (Subtask subtask : currentEpic.getSubtasks()) {
                    subtasksMap.remove(subtask.getId());
                }
            }
            epicsMap.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteSubtask(int id) {
        if (subtasksMap.containsKey(id)) {
            Subtask currentSubtask = subtasksMap.get(id);
            int epicId = currentSubtask.getEpicsId();
            if (epicsMap.containsKey(epicId)) {
                Epic currentEpic = epicsMap.get(epicId);
                if (!currentEpic.deleteSubtask(currentSubtask)) {
                    System.out.println("Непредвиденная ошибка! При удалении подзадачи id=" + currentSubtask.getId()
                    + "такой не нашлось в объекте Эпик id=" + currentEpic.getId());
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

    public int getNewId() {
        return this.currentId++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        TaskManager taskManager = (TaskManager) obj;
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
        String result =  "TaskManager{" +
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

    private <T> ArrayList<Integer> getSortedKeysOfTaskMap(HashMap<Integer, T> map) {
        ArrayList<Integer> keysSortedList = new ArrayList<>(map.keySet());
        Collections.sort(keysSortedList);
        return keysSortedList;
    }

    private void updateEpicsSubtasks(Epic oldEpic, Epic newEpic) {
        ArrayList<Subtask> oldSubtasks = oldEpic.getSubtasks();
        ArrayList<Subtask> newSubtasks = newEpic.getSubtasks();
        if (oldSubtasks == null && newSubtasks == null) return;
        if (oldSubtasks == null) {
            for (Subtask newSubtask : newSubtasks) {
                addSubtask(newSubtask);
            }
            return;
        }

        if (newSubtasks == null) {
            for (Subtask oldSubtask : oldSubtasks) {
                subtasksMap.remove(oldSubtask.getId());
                oldEpic.deleteSubtask(oldSubtask.getId());
            }
            return;
        }

        for (Subtask oldSubtask : oldSubtasks) {
            if (!newSubtasks.contains(oldSubtask)) {
                subtasksMap.remove(oldSubtask.getId());
                oldEpic.deleteSubtask(oldSubtask.getId());
            }
        }

        for (Subtask newSubtask : newSubtasks) {
            if (!oldSubtasks.contains(newSubtask)) {
                subtasksMap.put(newSubtask.getId(), newSubtask);
                oldEpic.addSubtask(newSubtask);
            }
        }
    }
}
