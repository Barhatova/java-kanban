package model;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class SubTask extends Task {

    Integer epicId;
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

    public SubTask(int id, TypeTask type, String name, String description, Status status, Integer epicId,
                   LocalDateTime startTime, Duration duration) {
        super(id, type, name, description, status, startTime, duration);
        this.type = type;
        this.epicId = epicId;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plus(duration.toMinutes(), ChronoUnit.MINUTES);
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

