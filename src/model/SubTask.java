package model;

import java.util.Objects;

public class SubTask extends Task {

    public Epic epic;
    private int epicId;

    public SubTask(String name, String description, Status status, Epic epic, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
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
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}

