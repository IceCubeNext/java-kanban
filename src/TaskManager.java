import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager implements ITaskManager{
    private int currentId;
    HashMap<Integer, Task> tasksMap = new HashMap<>();
    HashMap<Integer, Epic> epicsMap = new HashMap<>();
    HashMap<Integer, Subtask> subtasksMap = new HashMap<>();

    public ArrayList<Task> getTasks(){
        return new ArrayList<>();
    };

    public ArrayList<Epic> getEpics(){
        return new ArrayList<>();
    };

    public ArrayList<Subtask> getSubtasks(){
        return new ArrayList<>();
    };

    public Task getTask(int id){
        return new Task(0, "name", "description");
    };

    public Epic getEpic(int id){
        return new Epic(0, "name", "description", null);
    };

    public Subtask getSubtask(int id){
        return new Subtask(0, 0, "name", "description");
    };

    public ArrayList<Subtask> getEpicSubtasks(int epicId){
        return new ArrayList<>();
    };

    public Subtask getEpicSubtask(int epicId){
        return new Subtask(0, 0,"name", "description");
    };

    public Epic getSubtasksEpic(int subtasksId){
        return new Epic(0, "name", "description", null);
    };

    public boolean deleteTasks(){
        return false;
    };

    public boolean deleteEpics(){
        return false;
    };

    public boolean deleteSubtasks(){
        return false;
    };

    public boolean deleteTask(int id){
        return false;
    };

    public boolean deleteEpic(int id){
        return false;
    };

    public boolean deleteSubtask(int id){
        return false;
    };

    public int createTask(Task task){
        return 0;
    };

    public int createEpic(Epic epic){
        return 0;
    };

    public int createSubtask(Subtask subtask){
        return 0;
    };

    public boolean updateTask(Task task){
        return false;
    };

    public boolean updateEpic(Epic epic){
        return false;
    };

    public boolean updateSubtask(Subtask subtask){
        return false;
    };

    public int getNewId(){
        return this.currentId++;
    };

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
    public String toString(){
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
}
