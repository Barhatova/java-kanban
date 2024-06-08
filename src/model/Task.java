package model;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private Status status;
    private int id;
    protected TypeTask type;

    public Task(int id, TypeTask type, String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.type = type;
    }

    public Integer getEpicId() {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(description, task.description) &&
                Objects.equals(name, task.name) && Objects.equals(status, task.status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ",type=" + TypeTask.TASK +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
