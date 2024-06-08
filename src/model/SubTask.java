package model;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class SubTask extends Task {

    Integer epicId;
    public LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

    public SubTask(int id, TypeTask type, String name, String description, Status status, Integer epicId,
                   LocalDateTime startTime, Duration duration) {
        super(id, type, name, description, status, startTime, duration);
        this.type = type;
        this.epicId = epicId;
    }

    public SubTask(int id, TypeTask type, String name, String description, Status status, Integer epicId) {
        super(id, type, name, description, status);
        this.type = type;
        this.epicId = epicId;
    }

    @Override
    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
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



