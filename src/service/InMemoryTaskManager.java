package service;
import exception.*;
import model.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

public class InMemoryTaskManager implements TaskManager {

    protected static HashMap<Integer, Task> tasks;
    protected static HashMap<Integer, SubTask> subTasks;
    protected static HashMap<Integer, Epic> epics;
    protected HistoryManager historyManager;
    protected static int id = 0;
    protected int seqIdGenerationBase;

    TreeSet<Task> prioritizedTasks = new TreeSet<>(comparing(Task::getStartTime, nullsLast(LocalDateTime::compareTo)));

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        seqIdGenerationBase = 0;
    }

    void checkTaskTime(Task task) {
        for (Task t : prioritizedTasks) {
            if (t.getId() == task.getId()) {
                continue;
            }
            if ((t.getEndTime().isAfter(task.getStartTime()) && task.getStartTime().isBefore(t.getEndTime())) ||
                    (t.getStartTime().equals(task.getStartTime()) && task.getEndTime().equals(t.getEndTime())) ||
                    (t.getEndTime().equals(task.getStartTime()) && task.getStartTime().equals(t.getEndTime()))) {
                throw new ValidationException("Пересечение с задачей:" + task);
            }
        }
    }

    private int generateId() {
        return id++;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        subTask.setId(generateId());
        prioritizedTasks.add(subTask);
        checkTaskTime(subTask);
        subTasks.put(subTask.getId(), subTask);
        if (epic != null) {
            epic.addSubTask(subTask);
            calculateEpicStatus(epic);
        }
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        calculateEpicStatus(epic);
        return epic;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public void updateTask(Task task) {
        Task updateTask = tasks.get(task.getId());
        if (updateTask == null) {
            throw new NotFoundException("Не найдена задача: " + task.getId());
        }
        checkTaskTime(task);
        prioritizedTasks.remove(updateTask);
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Integer epicId = subTask.getEpicId();
        Epic savedEpic = epics.get(epicId);
        if (savedEpic == null) {
            throw new NotFoundException("Не найден эпик : " + savedEpic);
        }
        calculateEpicStatus(savedEpic);
        checkTaskTime(subTask);
        prioritizedTasks.add(subTask);
        tasks.put(subTask.getId(), subTask);
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
    public SubTask getSubTaskById(int subTaskId) {
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
        for (SubTask subTask : epic.getSubTasks()) {
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
    public List<SubTask> getAllSubTasksForEpic(Epic epic) {
        List<SubTask> subTasksList = new ArrayList<>(epic.getSubTasks());
        return  subTasksList;
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
            } else if (subTask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subTask.getStartTime().isBefore(epic.getStartTime())) {
                epic.setStartTime(subTask.getStartTime());
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