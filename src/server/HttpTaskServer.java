package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import model.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import service.Managers;
import service.TaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int PORT = 8080;
    TaskManager manager;
    HttpServer taskServer;
    Gson gson;
    ErrorHandler errorHandler;

    public HttpTaskServer() {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
        this.gson = getGson();
        this.errorHandler = new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {

            }

            @Override
            public void error(SAXParseException exception) throws SAXException {

            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {

            }
        };

        try {
            taskServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskServer.createContext("/tasks", new TasksHandler(manager));
        taskServer.createContext("/epics", new EpicsHandler(manager));
        taskServer.createContext("/subtasks", new SubtasksHandler(manager));
        taskServer.createContext("/history", new HistoryHandler(manager));
        taskServer.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public void start() {
        taskServer.start();
        System.out.println("Сервер запущен на порту: " + PORT);
    }

    public void stop() {
        taskServer.stop(0);
        System.out.println("Сервер остановлен на порту: " + PORT);
    }

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        HttpTaskServer taskServer = new HttpTaskServer(manager);
        taskServer.start();

        Epic epic = new Epic(0, TypeTask.EPIC, "эпик", "описание эпика", Status.NEW,
                LocalDateTime.of(2024,06,07,18,00,00), Duration.ofMinutes(15));
        manager.createEpic(epic);

        SubTask s1 = new SubTask(1,TypeTask.SUBTASK,"подзадача1", "описаниеСабтаска", Status.NEW,
                epic.getId(), LocalDateTime.of(2024,06,07,18,20,00),
                Duration.ofMinutes(15));
        manager.createSubTask(s1);
        epic.addSubTask(s1);
        SubTask s2 = new SubTask(2,TypeTask.SUBTASK,"подзадача2", "описаниеСабтаска", Status.NEW,
                epic.getId(), LocalDateTime.of(2024,06,07,18,40,00),
                Duration.ofMinutes(15));
        manager.createSubTask(s2);
        epic.addSubTask(s2);

        Task task1 = new Task(3, TypeTask.TASK, "задача1", "описание1Задачи", Status.NEW,
                LocalDateTime.of(2024,06,07,19,00,00), Duration.ofMinutes(15));
        manager.createTask(task1);
        Task task2 = new Task(4, TypeTask.TASK, "задача2", "описание2Задачи", Status.NEW,
                LocalDateTime.of(2024,06,07,19,20,00), Duration.ofMinutes(15));
        manager.createTask(task2);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }
}
