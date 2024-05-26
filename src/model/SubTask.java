package model;
import java.util.Objects;

public class SubTask extends Task {

    int epicId;

    public SubTask(int id, TypeTask type, String name, String description, Status status, int epicId) {
        super(id, type, name, description, status);
        this.epicId = epicId;
        this.type = type;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getEpicId(), getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SubTask subTask = (SubTask) o;
        return getId() == subTask.getId() && Objects.equals(getDescription(), subTask.getDescription()) &&
                Objects.equals(getName(), subTask.getName()) && Objects.equals(getEpicId(), subTask.getEpicId());
    }


    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ",type=" + TypeTask.SUBTASK +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}



