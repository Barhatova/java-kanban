package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;
    private static int id = 0;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    private int generateId() {
        return id++;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask createSubtask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            subTask.setId(generateId());
            subTasks.put(subTask.getId(), subTask);
            epic.addSubTask(subTask);
            calculateEpicStatus(epic);
        }
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Task getTask(int id) {
        return tasks.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    @Override
    public Task updateTask(Task updatedTask) {
        tasks.put(updatedTask.getId(), updatedTask);
        return updatedTask;
    }

    @Override
    public SubTask updateSubtask(SubTask updatedSubtask) {
        subTasks.put(updatedSubtask.getId(), updatedSubtask);
        Epic epic = updatedSubtask.getEpic();
        calculateEpicStatus(epic);
        return updatedSubtask;
    }

    @Override
    public Epic updateEpic(Epic updatedEpic) {
        epics.put(updatedEpic.getId(), updatedEpic);
        return updatedEpic;
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubtaskById(int subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int subTaskId) {
        SubTask subtask = subTasks.get(subTaskId);
        if (subtask != null) {
            Epic epic = subtask.getEpic();
            if (epic != null) {
                epic.removeSubTask(subTaskId);
                calculateEpicStatus(epic);
            }
            subTasks.remove(subTaskId);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (SubTask subTask : epic.getSubTasks()) {
                subTasks.remove(subTask.getId());
            }
            epics.remove(epicId);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subTasks.clear();
        epics.clear();
    }

    @Override
    public List<SubTask> getAllSubtasksForEpic(Epic epic) {
        List<SubTask> subTasksList = new ArrayList<>(epic.getSubTasks());
        return subTasksList;
    }

    @Override
    public Status calculateEpicStatus(Epic epic) {
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}