package service;
import model.*;
import model.Status;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    SubTask createSubtask(SubTask subTask);

    Epic createEpic(Epic epic);

    void updateTask(Task updatedTask);

    void updateSubtask(SubTask subtask);

    void updateEpic(Epic epic);

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<Epic> getAllEpics();

    Task getTaskById(int taskId);

    SubTask getSubtaskById(int subTaskId);

    Epic getEpicById(int epicId);

    void deleteTaskById(int taskId);

    void deleteSubtaskById(int subTaskId);

    void deleteEpicById(int epicId);

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpics();

    List<SubTask> getAllSubtasksForEpic(Epic epic);

    Status calculateEpicStatus(Epic epic);

    List<Task> getHistory();
}