package service;

import model.*;
import exception.NotFoundException;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected static HashMap<Integer, Task> tasks;
    protected static HashMap<Integer, SubTask> subTasks;
    protected static HashMap<Integer, Epic> epics;
    protected HistoryManager historyManager;
    protected static int id = 0;
    protected int seqIdGenerationBase;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        seqIdGenerationBase = 0;
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
    public void updateTask(Task task) {
        Task updateTask = tasks.get(task.getId());
        if (updateTask == null) {
            throw new NotFoundException("Не найдена задача: " + task.getId());
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            throw new NotFoundException("Не найден эпик : " + subTask.getEpicId());
        }
        epic.removeSubTask(subTask);
        epic.addSubTask(subTask);
        calculateEpicStatus(epic);
        subTasks.put(subTask.getId(), subTask);
}

    @Override
    public void updateEpic(Epic epic) {
        Epic epicSaved = epics.get(epic.getId());
        if (epicSaved == null) {
            throw new NotFoundException("Не найден эпик : " + epic.getId());
        }
        epicSaved.setName(epic.getName());
        epicSaved.setDescription(epic.getDescription());
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new NotFoundException("Не найдена задача: " + task);
        }
        return task;
    }

    @Override
    public SubTask getSubtaskById(int subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);
        historyManager.add(subTask);
        if (subTask == null) {
            throw new NotFoundException("Не найдена подзадача: " + subTask);
        }
        return subTask;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        if (epic == null) {
            throw new NotFoundException("Не найден эпик: " + epic);
        }
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
        Task removeTask = tasks.remove(taskId);
        if (removeTask == null) {
            throw new NotFoundException("Не найдена задача: " + removeTask);
        } else {
            tasks.remove(taskId);
            historyManager.remove(taskId);
        }
    }

    @Override
    public void deleteSubtaskById(int subTaskId) {
        SubTask removeSubtask = subTasks.remove(subTaskId);
            Integer epicId = removeSubtask.getEpicId();
            Epic epicSaved = epics.get(epicId);
            calculateEpicStatus(epicSaved);
            historyManager.remove(subTaskId);
        if (removeSubtask == null) {
            throw new NotFoundException("Подзадача не найдена: " + removeSubtask);
        }
        }

    @Override
    public void deleteEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        for (SubTask subTask : epic.getListSubTasks()) {
            subTasks.remove(subTask.getId());
            }
            epics.remove(epicId);
        if (epic == null) {
            throw new NotFoundException("Не найден эпик: " + epic);
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
       // List<SubTask> subTasksList = new ArrayList<>(epic.getSubTasks());
      //  return  subTasksList;
        return epic.getListSubTasks();
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
