package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

import java.util.List;

public class EmptyTaskManager implements TaskManager {
    @Override
    public Task createTask(Task task) {
        return null;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        return null;
    }

    @Override
    public Epic createEpic(Epic epic) {
        return null;
    }

    @Override
    public void updateTask(Task updatedTask) {

    }

    @Override
    public void updateSubTask(SubTask subtask) {

    }

    @Override
    public void updateEpic(Epic epic) {

    }

    @Override
    public List<Task> getAllTasks() {
        return null;
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return null;
    }

    @Override
    public List<Epic> getAllEpics() {
        return null;
    }

    @Override
    public Task getTaskById(int taskId) {
        return null;
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) {
        return null;
    }

    @Override
    public Epic getEpicById(int epicId) {
        return null;
    }

    @Override
    public void deleteTaskById(int taskId) {

    }

    @Override
    public void deleteSubtaskById(int subTaskId) {

    }

    @Override
    public void deleteEpicById(int epicId) {

    }

    @Override
    public void deleteAllTasks() {

    }

    @Override
    public void deleteAllSubTasks() {

    }

    @Override
    public void deleteAllEpics() {

    }

    @Override
    public List<SubTask> getAllSubTasksForEpic(Epic epic) {
        return null;
    }

    @Override
    public Status calculateEpicStatus(Epic epic) {
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return null;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return null;
    }
}
