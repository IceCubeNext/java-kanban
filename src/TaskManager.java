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
        if (epicsMap.containsKey(subtask.getEpicsId())){
            int id = getNewId();
            subtask.setId(id);
            subtasksMap.put(id, subtask);
            return id;
        } else {
            System.out.println("Для добавляемой подзадачи не существует Эпика");
            return -1;
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Integer id: getSortedKeysOfTaskMap(tasksMap)){
            result.add(tasksMap.get(id));
        }
        return result;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> result = new ArrayList<>();
        for (Integer id: getSortedKeysOfTaskMap(epicsMap)){
            result.add(epicsMap.get(id));
        }
        return result;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> result = new ArrayList<>();
        for (Integer id: getSortedKeysOfTaskMap(subtasksMap)){
            result.add(subtasksMap.get(id));
        }
        return result;
    }

    public Task getTask(int id) {
        if (tasksMap.containsKey(id)) {
            return tasksMap.get(id);
        } else {
            System.out.println("Ошибка! Не найдена задача с id=" + id);
            return null;
        }
    }

    public Epic getEpic(int id) {
        if (epicsMap.containsKey(id)) {
            return epicsMap.get(id);
        } else {
            System.out.println("Ошибка! Не найден Эпик с id=" + id);
            return null;
        }
    }

    public Subtask getSubtask(int id) {
        if (subtasksMap.containsKey(id)) {
            return subtasksMap.get(id);
        } else {
            System.out.println("Ошибка! Не найдена подзадача с id=" + id);
            return null;
        }
    }

    public ArrayList<Subtask> getEpicsSubtasks(int id) {
        if (epicsMap.containsKey(id)) {
            return epicsMap.get(id).getSubtasks();
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
            currentEpic.setSubtasks(epic.getSubtasks());
            return true;
        } else {
            System.out.println("Ошибка! Не найден Эпик с id=" + id);
            return false;
        }
    }

    public boolean updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        if (subtasksMap.containsKey(id)) {
            Subtask currentSubtask = subtasksMap.get(id);
            currentSubtask.setEpicsId(subtask.getEpicsId());
            currentSubtask.setName(subtask.getName());
            currentSubtask.setDescription(subtask.getDescription());
            currentSubtask.setDescription(subtask.getDescription());
            currentSubtask.setStatus(subtask.getStatus());
            return true;
        } else {
            System.out.println("Ошибка! Не найдена подзадача с id=" + id);
            return false;
        }
    }

    public boolean deleteTasks() {
        boolean result = true;
        for (Integer id : tasksMap.keySet()){
            result = result & deleteTask(id);
        }
        return result;
    }

    public boolean deleteEpics() {
        boolean result = true;
        for (Integer id : epicsMap.keySet()){
            result = result & deleteEpic(id);
        }
        return result;
    }

    public boolean deleteSubtasks() {
        boolean result = true;
        for (Integer id : subtasksMap.keySet()){
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
            for (Subtask subtask : epicsMap.get(id).getSubtasks()) {
                if (!deleteSubtask(subtask.getId())) {
                    System.out.println("Непредвиденная ошибка! При попытке удаления подзадач Эпика id=" + id
                                     + ", " + "подзадача id=" + subtask.getId());
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
                if (!currentEpic.deleteSubtask(currentSubtask)){
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
}
