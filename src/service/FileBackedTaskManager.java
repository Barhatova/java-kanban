package service;

import model.*;
import model.TypeTask;
import converter.TaskConverter;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
        file = Paths.get("resources/task.csv").toFile();
    }

    public FileBackedTaskManager(File file, Charset charset) {
        this(Managers.getDefaultHistory(), file);
    }

    public void init() {
        loadFromFile();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file, StandardCharsets.UTF_8);
        manager.init();
        return manager;
    }

    private static Task fromString(String value) {
        Task task = null;
        final String[] str = value.split(",");
        TypeTask type = TypeTask.valueOf(str[1]);
        int id;
        String name;
        String description;
        Status status;
        int epicId;
        switch (type) {
            case TASK:
                id = Integer.parseInt(str[0]);
                name = str[2];
                description = str[3];
                status = Status.valueOf(str[4]);
                task = new Task(id, type, name, description, status);
                break;
            case EPIC:
                id = Integer.parseInt(str[0]);
                name = str[2];
                description = str[3];
                status = Status.valueOf(str[4]);
                task = new Epic(id, type, name, description, status);
                break;
            case SUBTASK:
                id = Integer.parseInt(str[0]);
                name = str[2];
                description = str[3];
                status = Status.valueOf(str[4]);
                epicId = Integer.parseInt(str[5]);
                task = new SubTask(id, type, name, description, status, epicId);
                break;
        }
        return task;
    }

    private int getEpicId(int epicId) {
        return epicId;
    }

    protected static void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter("resources/task.csv",
                StandardCharsets.UTF_8))) {
            writer.append("id, type, name, description, status, epicId\n");
            writer.newLine();
            for (Task task : tasks.values()) {
                writer.write(TaskConverter.toString(task));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(TaskConverter.toString(epic));
                writer.newLine();
            }
            for (SubTask subTask : subTasks.values()) {
                writer.write(TaskConverter.toString(subTask));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка файла: resources/task.csv");
        }
    }

    private void loadFromFile() {
        List<Task> listTask = new ArrayList<>();
        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader("resources/task.csv",
                StandardCharsets.UTF_8))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                final Task task = fromString(line);
                listTask.add(task);
                final int id = task.getId();
                if (task.getType() == TypeTask.TASK) {
                    tasks.put(id, task);
                } else if (task.getType() == TypeTask.EPIC) {
                    epics.put(id, (Epic) task);
                } else {
                    subTasks.put(id, (SubTask) task);
                }
                if (maxId < id) {
                    maxId = id;
                }
                if (line.isEmpty()) {
                    break;
                }
            }
            for (SubTask subTask : subTasks.values()) {
                Epic epic = epics.get(subTask.getEpicId());
                epic.addSubTask(subTask);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка в файле: resources/task.csv");
        }
        seq = maxId;
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public SubTask createSubtask(SubTask subTask) {
        super.createSubtask(subTask);
        save();
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteSubtaskById(int subTaskId) {
        super.deleteSubtaskById(subTaskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public List<Task> getAllTasks() {
        super.getAllTasks();
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        super.getAllSubTasks();
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        super.getAllEpics();
        return new ArrayList<>(epics.values());
    }
}
