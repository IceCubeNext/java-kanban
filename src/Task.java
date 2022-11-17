public class Task {
    String name;
    String description;
    int id;
    Status status;

    Task(int id, String name, String description){
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
    }

    Task(String name, String description){
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

}
