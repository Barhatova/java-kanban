package service;

import model.Status;
import model.Task;
import model.SubTask;
import model.Epic;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;

    private static int id = 1;

    private int generateId() {
        return id++;
    }

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public SubTask createSubtask(SubTask subTask) {
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Task updateTask(Task updatedTask) {
        tasks.put(updatedTask.getId(), updatedTask);
        return updatedTask;
    }

    public SubTask updateSubtask(SubTask updatedSubtask) {
        subTasks.put(updatedSubtask.getId(), updatedSubtask);
        Epic epic = updatedSubtask.getEpic();
        calculateEpicStatus();
        return updatedSubtask;
    }

    public Epic updateEpic(Epic updatedEpic) {
        epics.put(updatedEpic.getId(), updatedEpic);
        return updatedEpic;
   }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
   }

    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public Task getTaskById(int taskId) {
        Task task = tasks.get( taskId);
        return task;
    }

    public SubTask getSubtaskById(int subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);
        return subTask;
    }

    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        return epic;
    }

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteSubtaskById(int subTaskId) {
        SubTask subtask = subTasks.get(subTaskId);
        if (subtask != null) {
            Epic epic = subtask.getEpic();
            if (epic != null) {
                epic.removeSubTask(subTaskId);
                calculateEpicStatus();
            }
            subTasks.remove(subTaskId);
        }
    }

    public void deleteEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (SubTask subTask : epic.getSubTasks()) {
                subTasks.remove(subTask.getId());}
            epics.remove(epicId);
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
           calculateEpicStatus();
        }
        subTasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public List<SubTask> getAllSubtasksForEpic(Epic epic) {
        List<SubTask> subTasksList = new ArrayList<>(epic.getSubTasks());
        return  subTasksList;
    }

    public Status calculateEpicStatus() {
        boolean allDone = true;
        boolean allNew = true;

        if (subTasks.isEmpty()) {
            return Status.NEW;
        }

        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subTask.getStatus() != Status.NEW) {
                allNew = false;
            }
        }

        if (allDone) {
            return Status.DONE;
        } else if (allNew) {
            return Status.NEW;
        } else {
            return Status.IN_PROGRESS;
        }
    }
}
