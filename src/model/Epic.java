package model;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Epic extends Task {

    private HashMap<Integer, SubTask> subTasks;
    private SubTask subTask;
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

    public Epic(int id, TypeTask type, String name, String description, Status status) {
        super(id, type, name, description, Status.NEW);
        this.subTasks = new HashMap<>();
        this.setStatus(Status.NEW);
        this.type = type;
    }

    public Epic(int id, TypeTask type, String name, String description) {
        super(id, type, name, description, Status.NEW, LocalDateTime.now(), Duration.ofMinutes(15));
        this.subTasks = new HashMap<>();
        this.setStatus(Status.NEW);
        this.type = type;
    }

    public Epic(int id, TypeTask type, String name, String description, Status status, LocalDateTime startTime,
                Duration duration) {
        super(id, type, name, description, status, startTime, duration);
        this.subTasks = new HashMap<>();
        this.setStatus(Status.NEW);
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (subTasks.isEmpty()) {
            return LocalDateTime.MAX;
        }
        List<SubTask> subtaskList = subTasks.values()
                .stream()
                .sorted(Comparator.comparing(SubTask::getEndTime).reversed())
                .collect(Collectors.toList());
        endTime = subtaskList.get(0).getEndTime();
        return endTime;
    }

    public Duration getDuration() {
        if (subTasks == null || subTasks.isEmpty()) {
            return Duration.ZERO;
        }

        Duration duration = Duration.ZERO;
        for (SubTask subtask : subTasks.values()) {
            duration = duration.plus(subtask.getDuration());
        }
        return duration;
    }

    public void addSubTask(SubTask subTask) {
        this.subTask = subTask;
        subTasks.put(subTask.getId(), subTask);
    }

    public void removeSubTask(int subTaskId) {
        subTasks.remove(subTaskId);
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteList() {
       subTasks.clear();
    }

    public Integer getEpicId() {
        return null;
    }

    @Override
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