package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private HashMap<Integer, SubTask> subTasks;
    private SubTask subTask;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subTasks = new HashMap<>();
        this.setStatus(Status.NEW);
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void addSubTask(SubTask subTask) {
        this.subTask = subTask;
        subTasks.put(subTask.getId(), subTask);
    }

    public void removeSubTask(int subTaskId) {
        subTasks.remove(subTaskId);
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
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}