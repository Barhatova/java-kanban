package service;
import model.*;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    SubTask createSubTask(SubTask subTask);

    Epic createEpic(Epic epic);

    void updateTask(Task updatedTask);

    void updateSubTask(SubTask subtask);

    void updateEpic(Epic epic);

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<Epic> getAllEpics();

    Task getTaskById(int taskId);

    SubTask getSubTaskById(int subTaskId);

    Epic getEpicById(int epicId);

    void deleteTaskById(int taskId);

    void deleteSubtaskById(int subTaskId);

    void deleteEpicById(int epicId);

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpics();


    List<SubTask> getAllSubTasksForEpic(Epic epic);

    Status calculateEpicStatus(Epic epic);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}