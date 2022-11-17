import java.util.ArrayList;

public interface ITaskManager {
    public ArrayList<Task> getTasks();
    public ArrayList<Epic> getEpics();
    public ArrayList<Subtask> getSubtasks();

    public Task getTask(int id);
    public Epic getEpic(int id);
    public Subtask getSubtask(int id);

    public ArrayList<Subtask> getEpicSubtasks(int epicId);
    public Subtask getEpicSubtask(int epicId);

    public Epic getSubtasksEpic(int subtasksId);

    public boolean deleteTasks();
    public boolean deleteEpics();
    public boolean deleteSubtasks();
    public boolean deleteTask(int id);
    public boolean deleteEpic(int id);
    public boolean deleteSubtask(int id);

    public int createTask(Task task);
    public int createEpic(Epic epic);
    public int createSubtask(Subtask subtask);

    public boolean updateTask(Task task);
    public boolean updateEpic(Epic epic);
    public boolean updateSubtask(Subtask subtask);

    public int getNewId();
}
