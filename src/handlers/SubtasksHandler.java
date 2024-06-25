package handlers;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import model.*;
import service.TaskManager;
import server.HttpTaskServer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpHandler;

public class SubtasksHandler implements HttpHandler {
    private final Gson gson = HttpTaskServer.getGson();
    private final TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        int statusCode;
        String response;

        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        switch (method) {
            case "GET":
                if (query == null) {
                    statusCode = 200;
                    response = HttpTaskServer.getGson().toJson(manager.getAllSubTasks());
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id:") + 3));
                        SubTask subTask = manager.getSubTaskById(id);
                        if (subTask != null) {
                            response = HttpTaskServer.getGson().toJson(subTask);
                            statusCode = 200;
                        } else {
                            response = "Подзадача по заданному id не найдена";
                            statusCode = 404;
                        }

                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                }
                break;
            case "POST":
                String bodyRequest = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    SubTask subtask = HttpTaskServer.getGson().fromJson(bodyRequest, SubTask.class);

                    SubTask newSubtask = new SubTask(subtask.getId(), subtask.getType(), subtask.getName(),
                            subtask.getDescription(), subtask.getStatus(), subtask.getEpicId());
                    if (subtask.getDuration() != null) {
                        newSubtask.setDuration(subtask.getDuration());
                    }
                    if (subtask.getStartTime() != null) {
                        newSubtask.setStartTime(subtask.getStartTime());
                    }
                    Integer id = subtask.getId();
                    if (manager.getSubTaskById(id) != null) {
                        manager.updateSubTask(subtask);
                        statusCode = 200;
                        response = "Подзадача с id:" + id + " обновлена";
                    } else {
                        SubTask subtaskCreated = manager.createSubTask(subtask);
                        System.out.println("CREATED SUBTASK: " + subtaskCreated);
                        Integer idCreated = subtaskCreated.getId();
                        statusCode = 201;
                        response = "Создана подзадача с id:" + idCreated;
                    }
                } catch (JsonSyntaxException e) {
                    response = "Неверный формат запроса";
                    statusCode = 400;
                } catch (RuntimeException e) {
                    statusCode = 406;
                    response = e.getMessage();
                }
                break;
            case "DELETE":
                response = "";
                query = exchange.getRequestURI().getQuery();
                if (query == null) {
                    manager.deleteAllSubTasks();
                    statusCode = 201;
                    exchange.sendResponseHeaders(statusCode,-1);
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id:") + 3));
                        manager.deleteSubtaskById(id);
                        statusCode = 201;
                        exchange.sendResponseHeaders(statusCode,-1);
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                }
                break;
            default:
                statusCode = 400;
                response = "Некорректный запрос";
        }
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
    }
}
