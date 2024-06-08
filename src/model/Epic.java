package model;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<SubTask> listSubTasks = new ArrayList<>();
    private SubTask subTask;

    public Epic(int id, TypeTask type, String name, String description, Status status) {
        super(id, type, name, description, status);
        this.setStatus(Status.NEW);
        this.type = type;
    }

    public void addSubTask(SubTask subTask) {
        listSubTasks.add(subTask);
    }

    public void removeSubTask(SubTask subTask) {
        listSubTasks.remove(subTask);
    }

    public ArrayList<SubTask> getListSubTasks() {
        return listSubTasks;
    }

    public void deleteList() {
        listSubTasks.clear();
    }

    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Epic epic = (Epic) o;
        return getId() == epic.getId() && Objects.equals(getName(), epic.getName()) &&
                Objects.equals(getDescription(), epic.getDescription());
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ",type=" + TypeTask.EPIC +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}